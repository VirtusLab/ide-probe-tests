package org.virtuslab.tests.pants

import org.junit.Test
import org.virtuslab.ideprobe.{DurationCheckFixture, WaitLogic}
import scala.concurrent.duration.DurationInt

class PantsOpenProjectBenchmark extends PantsTestSuite with DurationCheckFixture {
  private val waiting = WaitLogic.emptyNamedBackgroundTasks(atMost = 2.hours)

  @Test def pants(): Unit = {
    checkDuration("pants", openProjectWithPants(_, waiting))
  }

  @Test def bsp(): Unit = {
    checkDuration("bsp", openProjectWithBsp(_, waiting))
  }
}
