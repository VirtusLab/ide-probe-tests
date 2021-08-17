package org.virtuslab.tests.pants

import org.virtuslab.ideprobe.{Assertions, IdeProbeFixture}
import org.virtuslab.ideprobe.robot.RobotPluginExtension

object BUILDFilesTest extends IdeProbeFixture
  with RobotPluginExtension with Assertions {

  def main(args: Array[String]): Unit = {
    fixtureFromConfig().run { intelliJ =>
      intelliJ.probe.withRobot.openProject(intelliJ.workspace)
      intelliJ.probe.projectModel()
    }
  }

}
