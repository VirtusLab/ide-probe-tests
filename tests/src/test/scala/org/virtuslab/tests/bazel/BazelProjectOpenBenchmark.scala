package org.virtuslab.tests.bazel

import org.junit.Test
import org.virtuslab.ideprobe.DurationCheckFixture
import org.virtuslab.tests.bazel.OpenProjectTestBazel

class BazelProjectOpenBenchmark extends BazelTestSuite with DurationCheckFixture {
  @Test def bazel(): Unit = checkDuration("bazel", openProjectWithBazel(_))
}
