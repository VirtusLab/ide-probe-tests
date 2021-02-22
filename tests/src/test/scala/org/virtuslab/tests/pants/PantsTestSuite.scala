package org.virtuslab.tests.pants

import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.ideprobe.pants.{PantsPluginExtension, PantsPluginExtraExtensions}

trait PantsTestSuite
  extends IdeProbeTestSuite
    with PantsPluginExtension
    with PantsPluginExtraExtensions
