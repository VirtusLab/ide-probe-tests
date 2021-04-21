package org.virtuslab.tests

import org.virtuslab.ideprobe.bazel.BazelPluginExtension
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.ideprobe.pants.{PantsPluginExtension, PantsPluginExtraExtensions}

trait IdeProbeTest
  extends IdeProbeTestSuite
    with PantsPluginExtension
    with PantsPluginExtraExtensions
    with BazelPluginExtension
