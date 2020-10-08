package com.twitter.intellij.updatechecker

import java.nio.file.Path

import org.jetbrains.intellij.pluginRepository._
import org.jetbrains.intellij.pluginRepository.model.{PluginUpdateBean, ProductFamily}
import org.virtuslab.ideprobe.Config
import org.virtuslab.ideprobe.Extensions.PathExtension
import org.virtuslab.ideprobe.dependencies.Plugin
import org.virtuslab.ideprobe.dependencies.Plugin.Versioned

import scala.jdk.CollectionConverters._

object UpdateChecker {

  // Assuming this program is launched in the project's base directory
  private val configFile = Path.of("tests/src/test/resources/versions.conf")

  case class PluginUpdate(id: String, version: String)

  private val updateBeanOrdering = Ordering.fromLessThan[PluginUpdateBean] {
    case (x, y) => x.getCdate < y.getCdate
  }
  import updateBeanOrdering._

  private def updatedConfig(oldConfig: String, changes: Seq[PluginUpdate]): String = {
    changes.foldLeft(oldConfig){ case (config, PluginUpdate(id, version)) =>
      config.replaceFirst(
        s"""\\{ id = "$id", version = "[a-zA-Z0-9_\\-.]+"""",
        s"""{ id = "$id", version = "$version""""
      )
    }
  }

  def main(args: Array[String]): Unit = {

    println(s"Reading the conf file from $configFile")
    val config = Config.fromFile(configFile)
    val intellijBuildVersion = config[String]("versions.intellij.build")
    println(intellijBuildVersion)
    val currentPlugins = Seq("pythonCommunity", "scala", "pants", "thrift").map(p => config[Plugin](s"versions.plugins.$p"))

    println()
    println("Versions from the conf file:")
    currentPlugins.foreach {
      case Versioned(id, version, channel) =>
        println(s"  * ${id} ${version} ${channel.getOrElse("")}")
      case other =>
        println(s"  - ${other}")
    }

    val pluginRepository = PluginRepositoryFactory.create("https://plugins.jetbrains.com", null)
    val pluginManager = pluginRepository.getPluginManager
    val pluginUpdateManager = pluginRepository.getPluginUpdateManager

    println()
    println("Checking the plugin repository for updates...")
    println()

    def findUpdates(p: Versioned): Option[PluginUpdate] = {
      val updates = pluginManager.searchCompatibleUpdates(
        java.util.List.of(p.id), intellijBuildVersion, p.channel.getOrElse("")
      ).asScala.toList
      println(s"Updates for ${p.id} ${p.version}: ${updates.size}")
      updates.foreach(u => println("  " + u))
      def getUpdateInfo(id: String, version: String) = pluginUpdateManager.getUpdatesByVersionAndFamily(
        id, version, ProductFamily.INTELLIJ
      ).asScala.toList
      val availableUpdate = updates.head
      val availableVersion = availableUpdate.getVersion
      val currentUpdateInfo = getUpdateInfo(p.id, p.version).head
      val availableUpdateInfo = getUpdateInfo(p.id, availableVersion).head
      val isNewer = currentUpdateInfo < availableUpdateInfo
      println((if(isNewer) "!" else " ") + s" ${availableVersion} is ${if(isNewer) "" else "not "}newer than ${currentUpdateInfo.getVersion}")
      println()
      if(isNewer) Some(PluginUpdate(p.id, availableVersion)) else None
    }

    val pluginUpdates = currentPlugins.collect {
      case p: Versioned => findUpdates(p)
    }.flatten

    if(pluginUpdates.isEmpty) {
      println("All plugins are up to date!")
    } else {
      println("Some plugins are outdated!")
      println("Available updates:")
      pluginUpdates.foreach { u => println(s"  ${u.id} : " + u.version) }
      val oldConfig = configFile.content
      val updated = updatedConfig(oldConfig, pluginUpdates)
      configFile.write(updated)
      println(s"Updated the config file at ${configFile}")
    }
  }

}
