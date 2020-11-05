package com.twitter.intellij.pants

import org.junit.Assert._
import org.junit.Test
import org.virtuslab.ideprobe.ConfigFormat
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.{ProjectRef, TestRunConfiguration}

class RunFailedTestsTest extends PantsTestSuite with ConfigFormat {

  @Test def runTestsWithBsp(): Unit = {
    runTests("bsp", openProjectWithBsp, _.probe.build().assertSuccess())
  }

  @Test def runTestsWithPants(): Unit = {
    runTests("pants", openProjectWithPants, _.probe.compileAllTargets().assertSuccess())
  }

  //TODO add a test case for a Pants project (like in RunTestsTest)

  private def runTests(
    configSuffix: String,
    openProject: RunningIntelliJFixture => ProjectRef,
    buildProject: RunningIntelliJFixture => Unit
  ): Unit = {
    import pureconfig.generic.auto._
    fixtureFromConfig().run { intelliJ =>
      openProject(intelliJ)
      buildProject(intelliJ)

      val runConfiguration = intelliJ.config[TestRunConfiguration](s"runConfiguration.$configSuffix")
      val moduleName = runConfiguration.module.name

      val result = intelliJ.probe.run(runConfiguration)
      val errorsInitial = intelliJ.probe.errors
      assertEquals(s"number of test suites in in $moduleName", 1, result.suites.size)
      assertEquals("initial number of errors", errorsInitial.size, 2)
      assertEquals("initial test results", Set("Tests failed: 1, passed: 1"), errorsInitial.map(_.content).toSet)
      intelliJ.probe.invokeAction("RerunFailedTests")
      val errorsRerun = intelliJ.probe.errors.filterNot(errorsInitial.contains)
      assertEquals("number of rerun errors", errorsRerun.size, 2)
      assertEquals("rerun results", Set("Tests failed: 1, passed: 0"), errorsRerun.map(_.content).toSet)
    }
  }

}