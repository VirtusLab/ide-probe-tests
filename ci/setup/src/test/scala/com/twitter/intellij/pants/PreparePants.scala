package com.twitter.intellij.pants

import com.typesafe.config.ConfigObject
import java.net.URI
import org.junit.Test
import org.virtuslab.ideprobe.{Config, Id, IntelliJFixture}
import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.dependencies.{GitRepository, Plugin}
import org.virtuslab.ideprobe.pants.PantsSetup
import org.virtuslab.tests.pants.PantsTestSuite
import pureconfig.{ConfigSource, ConfigWriter}

class PreparePants extends PantsTestSuite {
  @Test def run(): Unit = {
    val pantsPluginRepository = "https://github.com/pantsbuild/intellij-pants-plugin"
    val repository = GitRepository(new URI(pantsPluginRepository), ref = None)
    val config = toBuildConfig(repository)
    val fixture = IntelliJFixture
      .fromConfig(Config.fromClasspath("pants.conf"))
      .withPlugin(Plugin.FromSources(Id("pants"), config))
    val workspace = fixture.setupWorkspace()
    val intellij = fixture.installIntelliJ()
    workspace.resolve("pants").write("dummy").makeExecutable()
    PantsSetup.overridePantsVersion(fixture, workspace)
    runPants(workspace, Seq("goals"))
    runFastpass(fixture.config, workspace, Seq("--version"))
    fixture.deleteIntelliJ(intellij)
    fixture.deleteWorkspace(workspace)
  }

  private def toBuildConfig(repository: GitRepository) = {
    val config = ConfigWriter[Map[String, GitRepository]]
      .to(Map("repository" -> repository))
      .asInstanceOf[ConfigObject]
      .toConfig
    Config(ConfigSource.fromConfig(config))
  }
}
