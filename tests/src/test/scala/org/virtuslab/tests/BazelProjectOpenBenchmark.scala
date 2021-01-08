package org.virtuslab.tests

import org.junit.Test
import org.virtuslab.ideprobe.robot.RobotPluginExtension

class BazelProjectOpenBenchmark extends Benchmark with BazelOpenProjectFixture with RobotPluginExtension {
  @Test def bazel(): Unit = benchmark("bazel", openProjectWithBazel)
}
