package org.virtuslab.tests.bazel

import org.virtuslab.ideprobe.bazel.BazelPluginExtension
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.tests.CommonExtensions

trait BazelTestSuite extends IdeProbeTestSuite with BazelPluginExtension with CommonExtensions
