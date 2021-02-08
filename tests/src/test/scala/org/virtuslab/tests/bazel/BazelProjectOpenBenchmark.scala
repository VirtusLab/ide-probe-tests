package org.virtuslab.tests.bazel

import org.junit.Test
import org.virtuslab.tests.Benchmark

class BazelProjectOpenBenchmark extends BazelTestSuite with Benchmark {

  @Test def bazel(): Unit = benchmark("bazel", openProjectWithBazel)

}
