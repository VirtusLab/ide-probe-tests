package org.virtuslab.tests.bazel

import org.junit.Test
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.tests.{OpenProjectTest, OpenProjectTestFixture}


object OpenProjectTestBazel
  extends BazelTestSuite
    with OpenProjectTestFixture {

  override def openProject(): ProjectRef = openProjectWithBazel(intelliJ)

}

// JDK11 only!
class OpenProjectTestBazel extends BazelTestSuite with OpenProjectTest {

  override def intelliJ: RunningIntelliJFixture = OpenProjectTestBazel.intelliJ

  @Test def buildSuccessful(): Unit = {
    val robot = intelliJ.probe.withRobot.robot
    intelliJ.probe.invokeAction("MakeBlazeProject")
    val buildLogs = robot.findAll(query.className("EditorComponentImpl"))
    assert(buildLogs.exists(_.fullText.contains("Build completed successfully, 1 total action")))
  }

  override def checkGitRepositoryRootDetected(): Unit = {
    println("Setting VCS root is not performed by bazel plugin")
  }
}
