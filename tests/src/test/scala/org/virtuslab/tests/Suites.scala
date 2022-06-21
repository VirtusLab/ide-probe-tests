package org.virtuslab.tests

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.virtuslab.tests.bazel._

@RunWith(classOf[Suite])
@SuiteClasses(
  Array(
    classOf[OpenProjectTestBazel],
  ))
class Suite1

@RunWith(classOf[Suite])
@SuiteClasses(
  Array(
    classOf[BazelProjectOpenBenchmark]
  ))
class Suite2
