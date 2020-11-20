package com.twitter.intellij.updatechecker

import java.nio.file.Paths

import org.jetbrains.intellij.pluginRepository._
import org.jetbrains.intellij.pluginRepository.model.{PluginUpdateBean, ProductFamily}
import org.virtuslab.ideprobe.Config
import org.virtuslab.ideprobe.Extensions.PathExtension
import org.virtuslab.ideprobe.dependencies.IntelliJVersion
import org.virtuslab.ideprobe.dependencies.Plugin.Versioned
import pureconfig.generic.auto._

import scala.collection.JavaConverters._
import scala.math.Ordering.Implicits._

object UpdateChecker {

  // Assuming this program is launched in the project's base directory
  private val configFile = Paths.get("tests/src/test/resources/versions.conf")

  case class PluginUpdate(id: String, version: String)

  private val updateBeanOrdering = Ordering.fromLessThan[PluginUpdateBean] {
    case (x, y) => x.getCdate < y.getCdate
  }
  import updateBeanOrdering._

  private val buildOrdering = Ordering.by[IntelliJVersion, Seq[Int]](_.build.split('.').map(_.toInt))

  private def updatedConfig(oldConfig: VersionsConf, changes: Iterable[PluginUpdate]): VersionsConf = {
    val newPlugins = oldConfig.plugins.map{
      case (key, oldPlugin@Versioned(id, _, _)) =>
        val newVersion = changes.iterator.find(_.id == id).map(_.version)
        key -> newVersion.fold(oldPlugin)(v => oldPlugin.copy(version = v))
      case other => other
    }
    oldConfig.copy(plugins = newPlugins)
  }

  def main(args: Array[String]): Unit = {

    val config = Config.fromFile(configFile).source.loadOrThrow[VersionsConfFile].versions
    val currentPlugins = config.plugins.values

    println()
    println(s"Versions from the conf file (${configFile}):")
    println(s"  * IntelliJ platform: ${config.intellij.build}")
    currentPlugins.foreach {
      case Versioned(id, version, channel) =>
        println(s"   * $id $version ${channel.getOrElse("")}")
      case other =>
        println(s"   - ${other}")
    }

    val intelliJUpdate = PlatformUpdateChecker.latestUpdateVersion()

    val pluginRepository = PluginRepositoryFactory.create("https://plugins.jetbrains.com", null)
    val pluginManager = pluginRepository.getPluginManager
    val pluginUpdateManager = pluginRepository.getPluginUpdateManager

    val hasIntelliJUpdate = {
      import buildOrdering._
      intelliJUpdate > config.intellij
    }

    val targetIntelliJ = if(hasIntelliJUpdate) intelliJUpdate.build else config.intellij.build

    def findUpdates(p: Versioned): Option[PluginUpdate] = {
      val updates = pluginManager.searchCompatibleUpdates(
        List(p.id).asJava, targetIntelliJ, p.channel.getOrElse("")
      ).asScala.toList
      println(s"Updates for ${p.id} ${p.version}: ${updates.size}")
      def getUpdateInfo(id: String, version: String) = pluginUpdateManager.getUpdatesByVersionAndFamily(
        id, version, ProductFamily.INTELLIJ
      ).asScala.toList
      if(updates.isEmpty) {
        println(s"! No updates found for ${p.id} ${p.version}. Something is not right...")
      }
      updates.headOption.flatMap { availableUpdate =>
        val availableVersion = availableUpdate.getVersion
        val currentUpdateInfo = getUpdateInfo(p.id, p.version).head
        val availableUpdateInfo = getUpdateInfo(p.id, availableVersion).head
        val isNewer = currentUpdateInfo < availableUpdateInfo
        println((if(isNewer) " !" else "  ") + s" ${p.id} ${availableVersion} is ${if(isNewer) "" else "not "}newer than ${currentUpdateInfo.getVersion}")
        if(isNewer) Some(PluginUpdate(p.id, availableVersion)) else None
      }
    }

    println()
    println(s"Checking the plugin repository for updates compatible with IntelliJ ${targetIntelliJ}...")
    println()

    val pluginUpdates = currentPlugins.collect {
      case p: Versioned => findUpdates(p)
    }.flatten
    println()

    if (hasIntelliJUpdate) {
      println(s"An IntelliJ platform update is available: ${intelliJUpdate.build}")
    } else {
      println("The IntelliJ platform is up to date!")
    }

    if (pluginUpdates.isEmpty) {
      println("All plugins are up to date!")
    } else {
      println("New plugin versions are available:")
      pluginUpdates.foreach { u => println(s"  ${u.id} : " + u.version) }
    }

    val configWithIntelliJ =
      if (hasIntelliJUpdate) config.copy(intellij = intelliJUpdate)
      else config
    val configWithIntelliJAndPlugins =
      if (pluginUpdates.isEmpty) configWithIntelliJ
      else updatedConfig(configWithIntelliJ, pluginUpdates)

    if (configWithIntelliJAndPlugins != config) {
      configFile.write(VersionsConfFormat.format(VersionsConfFile(configWithIntelliJAndPlugins)))
      PullRequestDescription.setPrBody(if(hasIntelliJUpdate) Some(intelliJUpdate) else None, pluginUpdates)
    }
  }

}
