package org.virtuslab.tests

import com.twitter.intellij.pants.{CommonOpenProjectTests, OpenProjectTestFixture}
import org.junit.Test
import org.virtuslab.bazelprobe.driver.BazelPluginExtension
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ProjectRef



object OpenProjectTestBazel extends OpenProjectTestFixture with BazelPluginExtension with BazelOpenProjectFixture
{
  override def openProject(): ProjectRef = openProjectWithBazel(intelliJ)

  def testBuild(): Unit = {
    val robot = intelliJ.probe.withRobot.robot
    intelliJ.probe.invokeAction("MakeBlazeProject")
    val buildLogs = robot.findAll(query.className("EditorComponentImpl"))
    assert(buildLogs.exists(_.fullText.contains(" Build completed successfully, 1 total action")))
  }
}


// JDK11 only!!
class OpenProjectTestBazel extends CommonOpenProjectTests  {
  @Test def buildSuccessful(): Unit = {
    OpenProjectTestBazel.testBuild()
  }

  override def intelliJ: RunningIntelliJFixture = OpenProjectTestBazel.intelliJ
}

