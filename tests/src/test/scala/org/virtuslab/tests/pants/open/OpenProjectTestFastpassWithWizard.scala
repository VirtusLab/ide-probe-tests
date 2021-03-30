package org.virtuslab.tests.pants.open

import org.virtuslab.ideprobe.{RunningIntelliJFixture, WaitLogic}
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.ideprobe.scala.ScalaPluginExtension
import org.virtuslab.tests.OpenProjectTest


object OpenProjectTestFastpassWithWizard
  extends OpenProjectTestFixturePants
    with ScalaPluginExtension {

  override def openProject(): ProjectRef = {
    // note that for this scenario only single directory can be selected
    val relativePath = targetsFromConfig(intelliJ).head.stripSuffix("::")
    val path = intelliJ.workspace.resolve(relativePath)
    intelliJ.probe.importBspProject(path, intelliJ.probe.withRobot.extendWaitLogic(WaitLogic.Default))
  }

  private def targetsFromConfig(intelliJ: RunningIntelliJFixture): Seq[String] = {
    intelliJ.config[Seq[String]]("pants.import.targets")
  }
}

class OpenProjectTestFastpassWithWizard extends OpenProjectTest {
  override def intelliJ: RunningIntelliJFixture = OpenProjectTestFastpassWithWizard.intelliJ
}
