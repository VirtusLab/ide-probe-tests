package org.virtuslab.tests.pants

import org.virtuslab.ideprobe.{RunningIntelliJFixture, WaitLogic}
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.ideprobe.pants.{PantsPluginExtension, PantsPluginExtraExtensions}
import org.virtuslab.ideprobe.protocol.ProjectRef

trait PantsTestSuite
  extends IdeProbeTestSuite
    with PantsPluginExtension
    with PantsPluginExtraExtensions
//{
//  override def openProjectWithPants(intelliJ: RunningIntelliJFixture, waitLogic: WaitLogic): ProjectRef = {
//    val projectPath = runPantsIdeaPlugin(intelliJ.workspace, targetsFromConfig(intelliJ))
//    intelliJ.probe.openProject(projectPath, waitLogic)
//  }
//
//  private def targetsFromConfig(intelliJ: RunningIntelliJFixture): Seq[String] = {
//    intelliJ.config[Seq[String]]("pants.import.targets")
//  }
//}