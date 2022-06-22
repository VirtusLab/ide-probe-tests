package org.virtuslab.tests.bazel

import com.intellij.remoterobot.RemoteRobot
import java.nio.file.Path
import java.util.UUID
import org.virtuslab.ideprobe.bazel.BazelPluginExtension
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.tests.CommonExtensions
import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.WaitLogic
import org.virtuslab.ideprobe.bazel.BazelImportSpec
import org.virtuslab.ideprobe.protocol.ProjectRef

trait BazelTestSuite extends IdeProbeTestSuite with BazelPluginExtension with CommonExtensions {
  registerFixtureTransformer(_.withAfterWorkspaceSetup { (intelliJ, workspace) =>
    intelliJ.config.get[String]("bazel.version").foreach { version =>
      workspace.resolve(".bazelversion").write(version)
    }
  })

  override def openProjectWithBazel(
    intelliJ: RunningIntelliJFixture,
    waitLogic: WaitLogic = WaitLogic.Default
  ): ProjectRef = {
    val importSpec = intelliJ.config[BazelImportSpec]("bazel.import")
    val robotDriver = intelliJ.probe.withRobot
    importProject(intelliJ, importSpec, intelliJ.workspace, robotDriver.extendWaitLogic(waitLogic))
  }

  private def importProject(
    intelliJ: RunningIntelliJFixture,
    importSpec: BazelImportSpec,
    workspace: Path,
    waitLogic: WaitLogic
  ): ProjectRef = {
    val robot = intelliJ.probe.withRobot.robot
    val projectView = prepareBazelProjectViewFile(importSpec, workspace)

    // delete previous project
    workspace.resolve(".ijwb").delete()

    // start project import wizard
    intelliJ.probe.invokeActionAsync("Blaze.ImportProject2")

    // set workspace/repository root path
    robot
      .find(query.className("TextFieldWithHistory"))
      .setText(workspace.toRealPath().toString)
    robot.clickButton("Next")

    // set project view configuration file
    robot.clickRadioButton("Import project view file")
    robot.find(query.div("name" -> "projectview-file-path-field")).setText(projectView.toString)
    robot.clickButton("Next")

    // complete import wizard
    robot.clickButton("Create")

    intelliJ.probe.await(waitLogic)

    intelliJ.probe.listOpenProjects().head
  }

  private def prepareBazelProjectViewFile(importSpec: BazelImportSpec, workspace: Path): Path = {
    val text =
      s"""${section("directories", importSpec.directories)}
         |
         |derive_targets_from_directories: true
         |
         |${section("additional_languages", importSpec.languages)}""".stripMargin
    val file = workspace.resolve(s"ide-probe${UUID.randomUUID()}.viewconfig")
    file.write(text)
    file.toFile.deleteOnExit()
    workspace.relativize(file)
  }

  private def section(name: String, elements: Seq[String]): String = {
    if (elements.isEmpty) {
      ""
    } else {
      s"$name:\n${indentStrings(elements)}\n"
    }
  }

  private def indentStrings(strs: Seq[String]): String = {
    strs.map("  " + _).mkString("\n")
  }

  private implicit class RemoteRobotExt(robot: RemoteRobot) {
    def clickButton(name: String): Unit = {
      robot.find(query.button(name)).doClick()
    }
    def clickRadioButton(name: String): Unit = {
      robot.find(query.radioButton(name)).doClick()
    }
  }


}
