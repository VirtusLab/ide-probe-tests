package com.twitter.intellij.pants

import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite

class PantsTestSuite
  extends IdeProbeTestSuite
    with PantsPluginExtension
    with OpenProjectFixture
