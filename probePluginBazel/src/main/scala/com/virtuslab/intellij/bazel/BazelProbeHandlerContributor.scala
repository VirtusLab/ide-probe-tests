package com.virtuslab.intellij.bazel

import com.google.idea.blaze.base.settings.BlazeUserSettings
import com.virtuslab.bazel.protocol.BazelEndpoints
import org.virtuslab.ideprobe.ProbeHandlerContributor
import org.virtuslab.ideprobe.ProbeHandlers.ProbeHandler
import org.virtuslab.ideprobe.handlers.IntelliJApi

class BazelProbeHandlerContributor extends ProbeHandlerContributor with IntelliJApi {
  override def registerHandlers(handler: ProbeHandler): ProbeHandler = {
    handler
      .on(BazelEndpoints.SetupBazelExecutable)(setupBazelExecutable)
  }

  def setupBazelExecutable(setup: String): Unit = {
    runOnUISync {
      write {
        BlazeUserSettings.getInstance().setBazelBinaryPath(setup)
      }
    }
  }
}
