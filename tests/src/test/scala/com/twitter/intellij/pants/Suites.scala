package com.twitter.intellij.pants

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(classOf[Suite])
@SuiteClasses(Array(
  classOf[OpenProjectTestFastpassWithCmdLine],
  classOf[OpenProjectTestPants],
  classOf[OpenProjectTestFastpassWithWizard],
  classOf[BUILDFilesTest],
  classOf[PantsSettingsTest],
  classOf[ThriftIdeaPluginTest]
))
class Suite1


@RunWith(classOf[Suite])
@SuiteClasses(Array(
  classOf[RunAppTest],
  classOf[RunTestsTest],
  classOf[RunFailedTestsTest]
))
class Suite2
