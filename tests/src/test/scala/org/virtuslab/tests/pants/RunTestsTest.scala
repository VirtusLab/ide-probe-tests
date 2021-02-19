package org.virtuslab.tests.pants

import org.junit.Assert._
import org.junit.Test
import org.virtuslab.ideprobe.ConfigFormat
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.{ProjectRef, TestScope}

class RunTestsTest extends PantsTestSuite with ConfigFormat {

  @Test def runTestsWithBsp(): Unit = {
    runTests("bsp", openProjectWithBsp, _.probe.build().assertSuccess())
  }

  @Test def runTestsWithPants(): Unit = {
    runTests("pants", openProjectWithPants, _.probe.compileAllTargets().assertSuccess())
  }

  private def runTests(
    configSuffix: String,
    openProject: RunningIntelliJFixture => ProjectRef,
    buildProject: RunningIntelliJFixture => Unit
  ): Unit = {
    import pureconfig.generic.auto._
    fixtureFromConfig().run { intelliJ =>
      openProject(intelliJ)
      buildProject(intelliJ)

      val runConfiguration = intelliJ.config[TestScope.Package](s"runConfiguration.$configSuffix")
      val moduleName = runConfiguration.module.name

      val result = intelliJ.probe.runJUnit(runConfiguration)
      assertTrue(s"There were no suites in $moduleName", result.suites.nonEmpty)
      assertTrue(s"Tests failed in $moduleName with $result", result.isSuccess)
    }
  }

}
