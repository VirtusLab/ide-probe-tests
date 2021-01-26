package org.virtuslab.tests

import com.twitter.intellij.pants.Benchmarks
import org.virtuslab.ideprobe.{IdeProbeFixture, RunningIntelliJFixture}
import scala.concurrent.duration.Duration

trait Benchmark extends IdeProbeFixture {

  def benchmark(configPrefix: String, benchmarkedAction: RunningIntelliJFixture => Unit): Unit = {
    val fixture = fixtureFromConfig()
    val expectedDuration = fixture.config[Duration](s"$configPrefix.benchmark.expectedDuration")
    val maxDuration = fixture.config
      .get[Duration](s"$configPrefix.benchmark.maxDuration").getOrElse(expectedDuration * 2)
    Benchmarks.withRunningIntelliJ(expectedDuration, maxDuration, fixture) { intelliJ =>
      benchmarkedAction(intelliJ)
    }
  }

}
