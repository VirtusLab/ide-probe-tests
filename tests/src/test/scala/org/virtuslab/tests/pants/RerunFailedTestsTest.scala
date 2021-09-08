package org.virtuslab.tests.pants

import org.junit.Assert._
import org.junit.Test
import org.virtuslab.ideprobe.{RunningIntelliJFixture, WaitLogic}
import org.virtuslab.ideprobe.protocol.{ProjectRef, TestScope}
import scala.concurrent.duration.DurationInt

class RerunFailedTestsTest extends PantsTestSuite {

  @Test def runTestsWithBsp(): Unit = {
    runTests("bsp", openProjectWithBsp(_), _.probe.build().assertSuccess())
  }

  private def runTests(
    configPrefix: String,
    openProject: RunningIntelliJFixture => ProjectRef,
    buildProject: RunningIntelliJFixture => Unit
  ): Unit = {
    fixtureFromConfig().run { intelliJ =>
      openProject(intelliJ)
      buildProject(intelliJ)

      val runConfiguration = intelliJ.config[TestScope](s"$configPrefix.runConfiguration")
      val runnerName = intelliJ.config.get[String](s"$configPrefix.runnerName")
      val moduleName = runConfiguration.module.name

      val result = intelliJ.probe.runTestsFromGenerated(runConfiguration, runnerName)

      // Running tests endpoint does not wait for any background tasks, because waiting
      // for tests results is handled inside IntelliJ.
      // This test however reads results from logs because the endpoint os not fully
      // correct in terms of what it returns. See https://github.com/VirtusLab/ide-probe/issues/117
      // Hence we need to wait for logs to appear before accessing them.
      intelliJ.probe.await(WaitLogic.constant(1.second))

      val errorsInitial = getTestsMessages(intelliJ)

      assertEquals(s"number of test suites in in $moduleName", 1, result.suites.size)
      assertEquals("initial number of errors", 2, errorsInitial.size)

      assertEquals(
        "initial test results",
        Set("Tests failed: 1, passed: 1"),
        errorsInitial.map(_.content).toSet)
      intelliJ.probe.invokeAction("RerunFailedTests")
      val errorsRerun = getTestsMessages(intelliJ).filterNot(errorsInitial.contains)
      assertEquals("number of rerun errors", errorsInitial.size, 2)
      assertEquals(
        "rerun results",
        Set("Tests failed: 1, passed: 0"),
        errorsRerun.map(_.content).toSet)
    }
  }

  private def getTestsMessages(intelliJ: RunningIntelliJFixture) = {
    intelliJ.probe.errors.filter(err => err.content.contains("Tests failed"))
  }
}
