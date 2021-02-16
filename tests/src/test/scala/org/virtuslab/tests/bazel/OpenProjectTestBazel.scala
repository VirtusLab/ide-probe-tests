package org.virtuslab.tests.bazel

import org.junit.{Assert, Test}
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.{ProjectRef, TestScope}
import org.virtuslab.tests.{OpenProjectTest, OpenProjectTestFixture}


object OpenProjectTestBazel
  extends BazelTestSuite
    with OpenProjectTestFixture {

  override def openProject(): ProjectRef = openProjectWithBazel(intelliJ)

}

// JDK11 only!
class OpenProjectTestBazel extends BazelTestSuite with OpenProjectTest {

  override def intelliJ: RunningIntelliJFixture = OpenProjectTestBazel.intelliJ

  @Test override def checkGitRepositoryRootDetected(): Unit = {
    println("Git repository root is not supported in bazel")
  }

  @Test override def checkExpectedName(): Unit = {
    val expectedProject = intelliJ.config[String]("project.name")
    val projectModel = intelliJ.probe.projectModel()
    Assert.assertTrue(
      s"Project name is ${projectModel.name}, while name is expected to contain $expectedProject",
      projectModel.name.contains(expectedProject)
    )
  }

  @Test def buildSuccessful(): Unit = {
    val robot = intelliJ.probe.withRobot.robot
    intelliJ.probe.invokeAction("MakeBlazeProject")
    val buildLogs = robot.findAll(query.className("EditorComponentImpl"))
    assert(buildLogs.exists(_.fullText.contains("Build completed successfully")))
  }

  @Test def testSuccessful() : Unit = {
    import pureconfig.generic.auto._
    intelliJ.probe.awaitIdle()
    val runConfig = intelliJ.config[TestScope.Method]("runConfiguration")
    intelliJ.probe.runTestsFromGenerated(runConfig)
  }
}
