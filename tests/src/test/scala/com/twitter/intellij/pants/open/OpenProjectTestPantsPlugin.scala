package com.twitter.intellij.pants.open

import com.twitter.intellij.pants.PantsProbeDriver
import com.twitter.intellij.pants.protocol.PythonFacet
import java.nio.file.Path
import org.junit.{Assert, Test}
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.{FileRef, ModuleRef, ProjectRef, RunFixesSpec}
import org.virtuslab.tests.OpenProjectTest

object OpenProjectTestPantsPlugin extends OpenProjectTestFixturePants {
  override def openProject(): ProjectRef = openProjectWithPants(intelliJ)
}

class OpenProjectTestPantsPlugin extends OpenProjectTest {
  override def intelliJ: RunningIntelliJFixture = OpenProjectTestPantsPlugin.intelliJ

  @Test override def checkGitRepositoryRootDetected(): Unit = {
    println("Git repository root is not supported in open source tests")
  }

  @Test def checkPythonFacetsSetup(): Unit = {
    def checkFacetsForModule(module: ModuleRef, facets: Seq[PythonFacet]): Unit = {
      Assert.assertTrue(s"Unexpected python facets $facets for $module", facets.size == 1)
      val Seq(facet) = facets
      Assert.assertTrue(s"No sdk in python facet $facet for $module", facet.sdk.isDefined)
      val Some(sdk) = facet.sdk
      Assert.assertTrue(s"Sdk does not have home path: $sdk for $module", sdk.homePath.nonEmpty)
      Assert.assertEquals(s"Incorrect sdk type for $module", "Python SDK", sdk.typeId)
    }

    val pantsDriver = PantsProbeDriver(intelliJ.probe)

    // check if python modules have python sdk set up
    val pythonModules = intelliJ.config[Set[String]]("project.pythonModules")

    intelliJ.probe.projectModel().modulesByNames(pythonModules).map(_.toRef()).foreach { module =>
      val facets = pantsDriver.getPythonFacets(module)
      checkFacetsForModule(module, facets)
    }

    // check if after running python facet inspection *all* modules have python sdk setup
    intelliJ.probe.runLocalInspection(
      "com.twitter.intellij.pants.inspection.PythonFacetInspection",
      FileRef(intelliJ.workspace.resolve(intelliJ.config[Path]("project.buildFile"))),
      RunFixesSpec.All
    )

    intelliJ.probe.projectModel().moduleRefs.foreach { ref =>
      val facets = pantsDriver.getPythonFacets(ref)
      checkFacetsForModule(ref, facets)
    }
  }

}

