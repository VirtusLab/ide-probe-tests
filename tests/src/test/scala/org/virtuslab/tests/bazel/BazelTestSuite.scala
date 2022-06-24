package org.virtuslab.tests.bazel

import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.bazel.BazelPluginExtension
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.tests.CommonExtensions

trait BazelTestSuite extends IdeProbeTestSuite with BazelPluginExtension with CommonExtensions {
  registerFixtureTransformer(_.withAfterWorkspaceSetup { (intelliJ, workspace) =>
    intelliJ.config.get[String]("bazel.version").foreach { version =>
      workspace.resolve(".bazelversion").write(version)
    }
  })
}
