package org.virtuslab.tests.bazel

import org.junit.Test
import org.virtuslab.ideprobe.DurationCheckFixture
import org.virtuslab.tests.IdeProbeTest

class BazelProjectOpenBenchmark extends IdeProbeTest with DurationCheckFixture {
  @Test def bazel(): Unit = checkDuration("bazel", openProjectWithBazel(_))
}
