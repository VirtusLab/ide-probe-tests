package com.twitter.intellij.pants

import com.typesafe.config.ConfigObject
import org.junit.Test
import org.virtuslab.ideprobe.{Config, IntelliJFixture}
import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.dependencies.Plugin
import org.virtuslab.ideprobe.pants.PantsSetup
import org.virtuslab.tests.pants.PantsTestSuite

class PreparePants extends PantsTestSuite {
  @Test def run(): Unit = {
    val fixture = IntelliJFixture
      .fromConfig(Config.fromClasspath("pants.conf"))
      .withPlugin(Plugin.Versioned("com.intellij.plugins.pants", version = "1.17.0.2ee673770d628ec4c49cddf49a15bc8c6ab2457c", channel = Some("bleedingedge")))
    val workspace = fixture.setupWorkspace()
    val intellij = fixture.installIntelliJ()
    workspace.resolve("pants").write("dummy").makeExecutable()
    PantsSetup.overridePantsVersion(fixture, workspace)
    runPants(workspace, Seq("help", "goals"))
    runFastpass(fixture.config, workspace, Seq("--version"))
    fixture.deleteIntelliJ(intellij)
    fixture.deleteWorkspace(workspace)
  }
}
