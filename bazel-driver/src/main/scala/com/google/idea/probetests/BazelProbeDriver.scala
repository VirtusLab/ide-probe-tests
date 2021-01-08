package com.google.idea.probetests

import com.virtuslab.bazel.protocol.BazelEndpoints
import org.virtuslab.ideprobe.ProbeDriver
import org.virtuslab.ideprobe.protocol.ProjectRef

class BazelProbeDriver(val driver: ProbeDriver) {
  def setupBazelExec(path: String): Unit = {
    driver.send(BazelEndpoints.SetupBazelExecutable, path)
  }
}
