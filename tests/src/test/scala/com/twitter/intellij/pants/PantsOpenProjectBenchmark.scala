package com.twitter.intellij.pants

import org.junit.Test
import org.virtuslab.tests.Benchmark

class PantsOpenProjectBenchmark extends PantsTestSuite with Benchmark {
  @Test def pants(): Unit = benchmark("pants", openProjectWithPants)

  @Test def bsp(): Unit = benchmark("bsp", openProjectWithBsp)
}


