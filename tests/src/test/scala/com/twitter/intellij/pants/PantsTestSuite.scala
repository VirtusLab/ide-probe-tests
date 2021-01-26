package com.twitter.intellij.pants

import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.ide.intellij.IntelliJFactory
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite

trait PantsTestSuite
  extends IdeProbeTestSuite
    with PantsPluginExtension {

  registerFixtureTransformer { fixture =>
    fixture.withAfterIntelliJInstall { (_, intelliJ) =>
      val plugins = intelliJ.paths.plugins
      plugins.resolve("Kotlin").delete()
      plugins.resolve("android").delete()
    }
  }

  registerFixtureTransformer { fixture =>
    fixture.withAfterIntelliJInstall { (_, intelliJ) =>
      intelliJ.paths.bin.resolve("printenv.py").makeExecutable()
    }
  }

  registerFixtureTransformer { fixture =>
    fixture.withAfterWorkspaceSetup { (_, workspace) =>
      BspWorkspaceMonitor.register(workspace)
    }
  }

}
