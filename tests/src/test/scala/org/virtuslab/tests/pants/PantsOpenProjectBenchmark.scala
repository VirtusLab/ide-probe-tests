package org.virtuslab.tests.pants

import org.junit.Test
import org.virtuslab.ideprobe.DurationCheckFixture
import org.virtuslab.tests.IdeProbeTest

class PantsOpenProjectBenchmark extends IdeProbeTest with DurationCheckFixture {
  @Test def pants(): Unit = checkDuration("pants", openProjectWithPants(_))

  @Test def bsp(): Unit = checkDuration("bsp", openProjectWithBsp(_))
}
