package org.virtuslab.tests

import com.intellij.remoterobot.fixtures.CommonContainerFixture
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.ideprobe.robot.RobotPluginExtension

trait BazelOpenProjectFixture extends BazeliskExtension { this: RobotPluginExtension =>
  implicit class SearchableComponentExt(sc: CommonContainerFixture) {
    def doClick(): Unit = sc.runJs("component.doClick();", true)
    def setText(text: String): Unit = sc.runJs(s"component.setText('$text');", true)
  }

  def openProjectWithBazel(intelliJ: RunningIntelliJFixture): ProjectRef = {
    val path = intelliJ.workspace.toRealPath()
    intelliJ.probe.setupBazelExec(bazelPath(intelliJ.workspace).toString)
    val viewFile = intelliJ.config[String]("viewConfig")
    intelliJ.probe.invokeActionAsync("Blaze.ImportProject2")
    val robot = intelliJ.probe.withRobot.robot
    robot.find(query.className("TextFieldWithHistory")).setText(path.toString)
    robot.find("//div[@accessiblename='Next' and @class='JButton']").doClick()
    robot.find("//div[@accessiblename='Import project view file' and @class='JRadioButton']").doClick()
    robot.find("//div[@name='projectview-file-path-field']").setText(viewFile)
    robot.find("//div[@accessiblename='Next' and @class='JButton']").doClick()
    robot.find("//div[@accessiblename='Finish' and @class='JButton']").doClick()
    intelliJ.probe.awaitIdle()
    intelliJ.probe.listOpenProjects().head
  }

}
