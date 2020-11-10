package com.twitter.intellij.updatechecker

import java.nio.file.Paths

import org.jetbrains.intellij.pluginRepository._
import org.jetbrains.intellij.pluginRepository.model.{PluginUpdateBean, ProductFamily}
import org.virtuslab.ideprobe.Config
import org.virtuslab.ideprobe.Extensions.PathExtension
import org.virtuslab.ideprobe.dependencies.IntelliJVersion
import org.virtuslab.ideprobe.dependencies.Plugin.Versioned
import pureconfig.generic.auto._

import scala.jdk.CollectionConverters._
import scala.math.Ordering.Implicits.seqOrdering

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

    println(s"Reading the conf file from $configFile")
    println()
    val config = Config.fromFile(configFile).source.loadOrThrow[VersionsConfFile].versions
    println(s"Platform version is ${config.intellij.build}")
    val currentPlugins = config.plugins.values

    println()
    println("Versions from the conf file:")
    currentPlugins.foreach {
      case Versioned(id, version, channel) =>
        println(s"  * $id $version ${channel.getOrElse("")}")
      case other =>
        println(s"  - ${other}")
    }

    println()
    println("Checking the platform repository for updates...")
    val intelliJUpdate = PlatformUpdateChecker.latestUpdateVersion()

    val pluginRepository = PluginRepositoryFactory.create("https://plugins.jetbrains.com", null)
    val pluginManager = pluginRepository.getPluginManager
    val pluginUpdateManager = pluginRepository.getPluginUpdateManager

    println()
    println("Checking the plugin repository for updates...")
    println()

    def findUpdates(p: Versioned): Option[PluginUpdate] = {
      val updates = pluginManager.searchCompatibleUpdates(
        List(p.id).asJava, config.intellij.build, p.channel.getOrElse("")
      ).asScala.toList
      println(s"Updates for ${p.id} ${p.version}: ${updates.size}")
      updates.foreach(u => println("  " + u))
      def getUpdateInfo(id: String, version: String) = pluginUpdateManager.getUpdatesByVersionAndFamily(
        id, version, ProductFamily.INTELLIJ
      ).asScala.toList
      if(updates.isEmpty) {
        println(s"No updates found for ${p.id} ${p.version}. Something is not right...")
      }
      updates.headOption.flatMap { availableUpdate =>
        val availableVersion = availableUpdate.getVersion
        val currentUpdateInfo = getUpdateInfo(p.id, p.version).head
        val availableUpdateInfo = getUpdateInfo(p.id, availableVersion).head
        val isNewer = currentUpdateInfo < availableUpdateInfo
        println((if(isNewer) "!" else " ") + s" ${availableVersion} is ${if(isNewer) "" else "not "}newer than ${currentUpdateInfo.getVersion}")
        println()
        if(isNewer) Some(PluginUpdate(p.id, availableVersion)) else None
      }
    }

    val pluginUpdates = currentPlugins.collect {
      case p: Versioned => findUpdates(p)
    }.flatten

    val hasIntelliJUpdate = {
      import buildOrdering._
      intelliJUpdate > config.intellij
    }

    if (hasIntelliJUpdate) {
      println(s"An IntelliJ platform update is available: ${intelliJUpdate.build}")
    } else {
      println("The IntelliJ platform is up to date!")
    }

    if (pluginUpdates.isEmpty) {
      println("All plugins are up to date!")
    } else {
      println("Some plugins are outdated!")
      println("Available updates:")
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
      println(s"Updated the config file at ${configFile}")
    }
  }

}
