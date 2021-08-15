package org.virtuslab.tests.pants

import org.junit.Test
import org.virtuslab.ideprobe.Assertions
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.ideprobe.robot.RobotPluginExtension

final class BUILDFilesTest extends IdeProbeTestSuite
  with RobotPluginExtension with Assertions {

  @Test def checkReferencesToOtherBUILDFiles(): Unit = {
    fixtureFromConfig().run { intelliJ =>
      intelliJ.probe.withRobot.openProject(intelliJ.workspace)
      intelliJ.probe.projectModel()
    }
  }

}
