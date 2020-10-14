package com.twitter.intellij.updatechecker

import com.typesafe.config.ConfigRenderOptions
import org.virtuslab.ideprobe.ConfigFormat
import org.virtuslab.ideprobe.dependencies.{IntelliJVersion, Plugin}
import pureconfig.ConfigWriter
import pureconfig.generic.semiauto.deriveWriter

case class VersionsConf(intellij: IntelliJVersion, plugins: Map[String, Plugin])

object VersionsConfFormat extends ConfigFormat {

  private case class VersionsConfProtocol(versions: VersionsConf)

  private val renderJson = ConfigRenderOptions
    .defaults().setComments(false).setFormatted(true).setOriginComments(false)

  private implicit val versionsWriter: ConfigWriter[VersionsConf] = deriveWriter[VersionsConf]

  private implicit val versionsConfProtocolWriter: ConfigWriter[VersionsConfProtocol] =
    deriveWriter[VersionsConfProtocol]

  def format(conf: VersionsConf): String = {
    val toWrite = VersionsConfProtocol(conf)
    ConfigWriter[VersionsConfProtocol].to(toWrite).render(renderJson)
  }

}