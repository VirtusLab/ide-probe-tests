package com.twitter.intellij.pants

import org.junit.Assert._
import org.junit.Test
import org.virtuslab.ideprobe.ConfigFormat
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.ideprobe.scala.ScalaPluginExtension
import org.virtuslab.ideprobe.scala.protocol.ScalaTestRunConfiguration

class RunFailedTestsTest extends PantsTestSuite with ConfigFormat with ScalaPluginExtension {

  @Test def runTestsWithBsp(): Unit = {
    runTests("bsp", openProjectWithBsp, _.probe.build().assertSuccess())
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

      val runConfiguration = intelliJ.config[ScalaTestRunConfiguration.Package](s"runConfiguration.$configSuffix")
      val moduleName = runConfiguration.module.name

      val result = intelliJ.probe.run(runConfiguration)
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