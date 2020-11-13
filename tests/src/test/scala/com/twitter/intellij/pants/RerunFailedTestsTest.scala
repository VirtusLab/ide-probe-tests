package com.twitter.intellij.pants

import org.junit.Assert._
import org.junit.Test
import org.virtuslab.ideprobe.{ConfigFormat, RunningIntelliJFixture}
import org.virtuslab.ideprobe.protocol.{ProjectRef, TestScope}

class RerunFailedTestsTest extends PantsTestSuite with ConfigFormat {

  @Test def runTestsWithBsp(): Unit = {
    runTests("bsp", openProjectWithBsp, _.probe.build().assertSuccess())
  }

  //TODO add a test case for a Pants project (like in RunTestsTest)

  private def runTests(
    configSuffix: String,
    openProject: RunningIntelliJFixture => ProjectRef,
    buildProject: RunningIntelliJFixture => Unit
  ): Unit = {
    fixtureFromConfig().run { intelliJ =>
      import pureconfig.generic.auto._

      openProject(intelliJ)
      buildProject(intelliJ)

      val runConfiguration = intelliJ.config[TestScope.Module](s"runConfiguration.$configSuffix")
      val runnerName = intelliJ.config.get[String](s"runConfiguration.$configSuffix.runnerName")
      val moduleName = runConfiguration.module.name

      val result = intelliJ.probe.runTestsFromGenerated(runConfiguration, runnerName)
      val errorsInitial = intelliJ.probe.errors
      assertEquals(s"number of test suites in in $moduleName", 1, result.suites.size)
      assertEquals("initial number of errors", errorsInitial.size, 2)
      assertEquals("initial test results", Set("Tests failed: 1, passed: 1"), errorsInitial.map(_.content).toSet)
      intelliJ.probe.invokeAction("RerunFailedTests")
      val errorsRerun = intelliJ.probe.errors.filterNot(errorsInitial.contains)
      assertEquals("number of rerun errors", errorsInitial.size, 2)
      assertEquals("rerun results", Set("Tests failed: 1, passed: 0"), errorsRerun.map(_.content).toSet)
    }
  }

}
