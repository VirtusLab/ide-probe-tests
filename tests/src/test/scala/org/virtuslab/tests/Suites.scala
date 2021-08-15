package org.virtuslab.tests

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses
import org.virtuslab.tests.pants._

@RunWith(classOf[Suite])
@SuiteClasses(
  Array(
    classOf[BUILDFilesTest]
  ))
class Suite1
