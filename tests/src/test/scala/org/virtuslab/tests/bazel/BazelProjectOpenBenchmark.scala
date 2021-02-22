package org.virtuslab.tests.bazel

import org.junit.Test
import org.virtuslab.ideprobe.DurationCheckFixture

class BazelProjectOpenBenchmark extends BazelTestSuite with DurationCheckFixture {

  @Test def bazel(): Unit = checkDuration("bazel", openProjectWithBazel)

}
