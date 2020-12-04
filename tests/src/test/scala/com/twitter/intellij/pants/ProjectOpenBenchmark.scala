package com.twitter.intellij.pants

import org.junit.Test
import org.virtuslab.ideprobe.RunningIntelliJFixture
import scala.concurrent.duration.Duration

class ProjectOpenBenchmark extends PantsTestSuite {

  @Test def pants(): Unit = benchmark("pants", openProjectWithPants)

  @Test def bsp(): Unit = benchmark("bsp", openProjectWithBsp)

  private def benchmark(configPrefix: String, openProject: RunningIntelliJFixture => Unit): Unit = {
    val fixture = fixtureFromConfig()
    val expectedDuration = fixture.config[Duration](s"$configPrefix.benchmark.expectedDuration")
    val maxDuration = fixture.config
      .get[Duration](s"$configPrefix.benchmark.maxDuration").getOrElse(expectedDuration * 2)
    Benchmarks.withRunningIntelliJ(expectedDuration, maxDuration, fixture) { intelliJ =>
      openProject(intelliJ)
    }
  }
}
