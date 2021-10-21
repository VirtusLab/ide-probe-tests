package org.virtuslab.tests.pants

import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.ideprobe.pants.{PantsPluginExtension, PantsPluginExtraExtensions}
import org.virtuslab.tests.CommonExtensions

trait PantsTestSuite
  extends IdeProbeTestSuite
    with PantsPluginExtension
    with PantsPluginExtraExtensions
    with CommonExtensions
