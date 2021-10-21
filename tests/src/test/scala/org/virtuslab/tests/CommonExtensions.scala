package org.virtuslab.tests

import org.virtuslab.ideprobe.Extensions.PathExtension
import org.virtuslab.ideprobe.IdeProbeFixture

trait CommonExtensions { this: IdeProbeFixture =>
  registerFixtureTransformer(_.withAfterIntelliJInstall((_, ij) => {
    ij.paths.bundledPlugins.resolve("packageSearch").delete()
  }))
}
