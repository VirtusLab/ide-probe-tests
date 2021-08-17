package org.virtuslab.tests.pants

import java.util.concurrent.Executors
import org.virtuslab.ideprobe.{Assertions, IdeProbeFixture, Shell}
import org.virtuslab.ideprobe.robot.RobotPluginExtension
import scala.concurrent.Future

object BUILDFilesTest extends IdeProbeFixture
  with RobotPluginExtension with Assertions {

  def main(args: Array[String]): Unit = {
    fixtureFromConfig().run { intelliJ =>
      intelliJ.probe.withRobot.openProject(intelliJ.workspace)
      intelliJ.probe.projectModel()
    }

    Future(Shell.run("jstack", ProcessHandle.current.pid.toString))(scala.concurrent.ExecutionContext.global)
}

}
