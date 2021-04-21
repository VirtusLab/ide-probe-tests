package org.virtuslab.tests.pants

import org.junit.Test
import org.virtuslab.ideprobe.{Assertions, ConfigFormat, RunningIntelliJFixture}
import org.virtuslab.ideprobe.protocol.NavigationQuery
import org.virtuslab.ideprobe.protocol.NavigationTarget
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.tests.IdeProbeTest

final class ThriftIdeaPluginTest extends IdeProbeTest with Assertions with ConfigFormat {

  @Test
  def findThriftFilesPants(): Unit = {
    findThriftFiles(openProjectWithPants(_))
  }

  @Test
  def findThriftFilesBsp(): Unit = {
    findThriftFiles(openProjectWithBsp(_))
  }

  def findThriftFiles(openProject: RunningIntelliJFixture => ProjectRef): Unit = {
    fixtureFromConfig().run { intelliJ =>
      import pureconfig.generic.auto._

      openProject(intelliJ)
      intelliJ.probe.await()

      val queryString = intelliJ.config[String]("query")
      val navigationQuery = NavigationQuery(queryString, includeNonProjectItems = true)

      val targets = intelliJ.probe.find(navigationQuery)

      val expectedTargets = intelliJ.config[Seq[NavigationTarget]]("navigationTargets")
      assertContains(targets)(expectedTargets: _*)
    }
  }
}
