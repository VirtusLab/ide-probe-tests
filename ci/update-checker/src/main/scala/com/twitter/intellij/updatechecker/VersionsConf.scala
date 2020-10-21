package com.twitter.intellij.updatechecker

import com.typesafe.config.ConfigRenderOptions
import org.virtuslab.ideprobe.ConfigFormat
import org.virtuslab.ideprobe.dependencies.{IntelliJVersion, Plugin}
import pureconfig.ConfigWriter
import pureconfig.generic.semiauto.deriveWriter

case class VersionsConfFile(versions: VersionsConf)
case class VersionsConf(intellij: IntelliJVersion, plugins: Map[String, Plugin], tools: Tools)
case class Tools(fastpass: FastpassVersion)
case class FastpassVersion(version: String)

object VersionsConfFormat extends ConfigFormat {

  private val renderJson = ConfigRenderOptions
    .defaults().setComments(false).setFormatted(true).setOriginComments(false).setJson(true)

  private implicit val fastpassVersionWriter: ConfigWriter[FastpassVersion] =
    deriveWriter[FastpassVersion]

  private implicit val toolsWriter: ConfigWriter[Tools] = deriveWriter[Tools]

  private implicit val versionsWriter: ConfigWriter[VersionsConf] = deriveWriter[VersionsConf]

  private implicit val versionsConfFileWriter: ConfigWriter[VersionsConfFile] =
    deriveWriter[VersionsConfFile]

  def format(toWrite: VersionsConfFile): String = {
    ConfigWriter[VersionsConfFile].to(toWrite).render(renderJson)
  }

}
