package org.virtuslab.tests.bazel

import org.junit.Test
import org.virtuslab.ideprobe.{DurationCheckFixture, WaitLogic}
import scala.concurrent.duration.DurationInt

class BazelProjectOpenBenchmark extends BazelTestSuite with DurationCheckFixture {
  private val waiting = WaitLogic.emptyNamedBackgroundTasks(atMost = 2.hours)

  @Test def bazel(): Unit = checkDuration("bazel", openProjectWithBazel(_, waiting))
}
