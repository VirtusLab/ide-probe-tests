package org.virtuslab.tests.bazel

import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.tests.OpenProjectTestFixture

object OpenProjectTestBazelWithCmdLine
  extends BazelTestSuite
    with OpenProjectTestFixture {

  override def openProject(): ProjectRef = ProjectRef.Default

}

// JDK11 only!
class OpenProjectTestBazelWithCmdLine extends OpenProjectTestBazel {

  override def intelliJ: RunningIntelliJFixture = OpenProjectTestBazelWithCmdLine.intelliJ

}
