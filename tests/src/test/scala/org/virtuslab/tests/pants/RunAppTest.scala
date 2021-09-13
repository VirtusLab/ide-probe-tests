package org.virtuslab.tests.pants

import org.junit.Assert
import org.junit.Test
import org.virtuslab.ideprobe.ConfigFormat
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ApplicationRunConfiguration
import org.virtuslab.ideprobe.protocol.ProjectRef

class RunAppTest extends PantsTestSuite with ConfigFormat {

  @Test
  def runsMainClassWithPants(): Unit = {
    runsMainClass("pants", openProjectWithPants(_), _.probe.compileAllTargets().assertSuccess())
  }

 @Test
  def runsMainClassWithBsp(): Unit = {
    runsMainClass("bsp", openProjectWithBsp(_), _.probe.build().assertSuccess())
  }

  def runsMainClass(
    configSuffix: String,
    openProject: RunningIntelliJFixture => ProjectRef,
    buildProject: RunningIntelliJFixture => Unit,
  ): Unit = {
    import pureconfig.generic.auto._
    fixtureFromConfig().run { intelliJ =>
      openProject(intelliJ)
      buildProject(intelliJ)

      val runConfig =
        intelliJ.config[ApplicationRunConfiguration](s"runConfiguration.$configSuffix")
      val result = intelliJ.probe.runApp(runConfig)

      val expectedExitCode = intelliJ.config[Int]("expectedExitCode")
      val expectedOutput = intelliJ.config[String]("expectedStdout")
      val expectedErrOutput = intelliJ.config[String]("expectedStderr")

      Assert.assertEquals(expectedExitCode, result.exitCode)
      Assert.assertTrue(
        s"Unexpected error output: ${result.stderr}",
        result.stderr.contains(expectedErrOutput))
      Assert.assertTrue(
        s"Unexpected output: ${result.stdout}",
        result.stdout.contains(expectedOutput))
    }
  }

}
