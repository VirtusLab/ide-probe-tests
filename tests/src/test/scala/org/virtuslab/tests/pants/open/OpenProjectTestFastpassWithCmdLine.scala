package org.virtuslab.tests.pants.open

import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.tests.OpenProjectTest

object OpenProjectTestFastpassWithCmdLine extends OpenProjectTestFixturePants {
  override def openProject(): ProjectRef = openProjectWithBsp(intelliJ)
}

class OpenProjectTestFastpassWithCmdLine extends OpenProjectTest {
  override def intelliJ: RunningIntelliJFixture = OpenProjectTestFastpassWithCmdLine.intelliJ
}
