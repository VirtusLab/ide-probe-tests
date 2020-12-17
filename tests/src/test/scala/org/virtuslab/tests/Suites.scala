package org.virtuslab.tests

import com.twitter.intellij.pants._
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(classOf[Suite])
@SuiteClasses(
  Array(
    classOf[OpenProjectTestFastpassWithCmdLine],
    classOf[OpenProjectTestPants],
    classOf[OpenProjectTestFastpassWithWizard],
    classOf[BUILDFilesTest],
    classOf[PantsSettingsTest],
    classOf[ThriftIdeaPluginTest]
  ))
class Suite2

@RunWith(classOf[Suite])
@SuiteClasses(
  Array(
    classOf[RunAppTest],
    classOf[RunTestsTest],
    classOf[RerunFailedTestsTest]
  ))
class Suite1

@RunWith(classOf[Suite])
@SuiteClasses(
  Array(
    classOf[PantsOpenProjectBenchmark],
    classOf[BazelProjectOpenBenchmark],
    classOf[OpenProjectTestBazel]
  ))
class Suite3
