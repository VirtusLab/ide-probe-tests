package com.twitter.intellij.pants

import java.net.URI

import org.junit.Test
import org.virtuslab.ideprobe.{Config, Id, IntelliJFixture}
import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.dependencies.Plugin
import org.virtuslab.ideprobe.dependencies.SourceRepository.Git

class PreparePants extends PantsTestSuite {
  @Test def run(): Unit = {
    val fixture = IntelliJFixture.fromConfig(Config.fromClasspath("pants.conf"))
      .withPlugin(Plugin.FromSources(Id("pants"), Git(new URI("https://github.com/pantsbuild/intellij-pants-plugin"), None)))
    val workspace = fixture.setupWorkspace()
    val intellij = fixture.installIntelliJ()
    workspace.resolve("pants").write("dummy").makeExecutable()
    PantsSetup.overridePantsVersion(fixture, workspace)
    runPants(workspace, Seq("goals"))
    runFastpass(fixture.config, workspace, Seq("--version"))
    fixture.deleteIntelliJ(intellij)
    fixture.deleteWorkspace(workspace)
  }
}
