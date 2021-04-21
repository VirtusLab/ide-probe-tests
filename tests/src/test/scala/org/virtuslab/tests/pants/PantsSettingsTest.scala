package org.virtuslab.tests.pants

import org.junit.Assert._
import org.junit.Test
import org.virtuslab.ideprobe.pants.protocol.PantsProjectSettingsChangeRequest
import org.virtuslab.ideprobe.protocol.Setting
import org.virtuslab.tests.IdeProbeTest

class PantsSettingsTest extends IdeProbeTest {

  @Test def importProjectWithCustomSettings(): Unit = {
    fixtureFromConfig().run { intelliJ =>
      val importSettings =
        PantsProjectSettingsChangeRequest(
          loadSourcesAndDocsForLibs = Setting.Changed(true),
          useIntellijCompiler = Setting.Changed(true)
        )

      val projectRoot = intelliJ.workspace.resolve(intelliJ.config[String]("targetPath"))
      intelliJ.probe.importProject(projectRoot, importSettings)

      val initialSettings = intelliJ.probe.getPantsProjectSettings()

      assertTrue(
        "'Load sources and docs for libs' was not set",
        initialSettings.loadSourcesAndDocsForLibs)
      assertTrue("'Use IntelliJ compiler' was not set", initialSettings.useIntellijCompiler)

      intelliJ.probe.setPantsProjectSettings(
        PantsProjectSettingsChangeRequest(useIntellijCompiler = Setting.Changed(false))
      )

      val finalSettings = intelliJ.probe.getPantsProjectSettings()
      assertFalse(
        "'Load sources and docs for libs' was not unset",
        finalSettings.useIntellijCompiler)
    }
  }

}
