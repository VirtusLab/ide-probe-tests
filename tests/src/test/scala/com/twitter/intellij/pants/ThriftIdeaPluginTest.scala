package com.twitter.intellij.pants

import org.junit.Test
import org.virtuslab.ideprobe.{Assertions, ConfigFormat, RunningIntelliJFixture}
import org.virtuslab.ideprobe.protocol.NavigationQuery
import org.virtuslab.ideprobe.protocol.NavigationTarget
import org.virtuslab.ideprobe.protocol.ProjectRef

final class ThriftIdeaPluginTest extends PantsTestSuite with Assertions with ConfigFormat {

  @Test
  def findThriftFilesPants(): Unit = {
    findThriftFiles(openProjectWithPants)
  }

  @Test
  def findThriftFilesBsp(): Unit = {
    findThriftFiles(openProjectWithBsp)
  }

  def findThriftFiles(openProject: RunningIntelliJFixture => ProjectRef): Unit = {
    fixtureFromConfig().run { intelliJ =>
      import pureconfig.generic.auto._

      openProject(intelliJ)

      val queryString = intelliJ.config[String]("query")
      val navigationQuery = NavigationQuery(queryString, includeNonProjectItems = true)

      val targets = intelliJ.probe.find(navigationQuery)

      val expectedTargets = intelliJ.config[Seq[NavigationTarget]]("navigationTargets")
      assertContains(targets)(expectedTargets: _*)
    }
  }
}
