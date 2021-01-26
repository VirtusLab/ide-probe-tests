package org.virtuslab.tests

import java.nio.file.Path
import org.junit.Assert.assertEquals
import org.junit.{Assert, Test}
import org.virtuslab.ideprobe.junit4.RunningIntelliJPerSuite
import org.virtuslab.ideprobe.protocol.{ModuleRef, ProjectRef, SourceFolder, VcsRoot}
import org.virtuslab.ideprobe.{ConfigFormat, IdeProbeFixture, IntelliJFixture, RunningIntelliJFixture, Shell}
import org.virtuslab.tests.OpenProjectTest.TestData
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

// This fixture requires to define how to open the project and is responsible
// for starting and opening intellij with this project once per test suite.
//
// Fixture should be used mixed in to companion object of an actual test.
// it provides `intelliJ` method. The return value of this method should be put
// as implementation of intelliJ method in the test class.
trait OpenProjectTestFixture
  extends IdeProbeFixture
    with RunningIntelliJPerSuite {

  override protected def baseFixture: IntelliJFixture = fixtureFromConfig()

  override def beforeAll(): Unit = {
    Shell.run(in = intelliJ.workspace, "git", "init")
    openProject()
  }

  def openProject(): ProjectRef
}

// basic test cases for open project
trait OpenProjectTest {

  def intelliJ: RunningIntelliJFixture

  @Test def checkExpectedName(): Unit = {
    val expectedProject = intelliJ.config[String]("project.name")
    val projectModel = intelliJ.probe.projectModel()
    Assert.assertEquals(expectedProject, projectModel.name)
  }

  @Test def checkExpectedModules(): Unit = {
    def relative(absolutePath: Path): Path =
      intelliJ.workspace.toRealPath().relativize(absolutePath)

    val projectModel = intelliJ.probe.projectModel()

    val expectedModules = intelliJ.config[Seq[TestData.Module]]("project.modules")
    val importedModules = projectModel.modules.map { module =>
      TestData.Module(
        module.name,
        module.contentRoots.all.map(sf =>
          TestData.SourceRoot(relative(sf.path), sf.kind, sf.packagePrefix)))
    }

    Assert.assertTrue(
      s"Expected modules: $expectedModules, actual modules: $importedModules (not a subset)",
      expectedModules.toSet.subsetOf(importedModules.toSet))
  }

  @Test def checkProjectSdkSet(): Unit = {
    val projectSdk = intelliJ.probe.projectSdk()
    Assert.assertTrue(s"Project without sdk", projectSdk.isDefined)
  }

  @Test def hasModuleSdksSet(): Unit = {
    val project = intelliJ.probe.projectModel()
    val pythonModules = intelliJ.config.get[Set[String]]("project.pythonModules").getOrElse(Nil)
    val allModules = intelliJ.config[Set[TestData.Module]]("project.modules").map(_.name)
    val expectedModulesWithSdk = allModules -- pythonModules

    val modulesWithoutSdk = project.modules
      .filter(module => module.kind.isDefined && expectedModulesWithSdk.contains(module.name))
      .map(m => ModuleRef(m.name))
      .filter(module => intelliJ.probe.moduleSdk(module).isEmpty)

    Assert.assertTrue(s"Modules without sdk: $modulesWithoutSdk", modulesWithoutSdk.isEmpty)
  }

  @Test def checkGitRepositoryRootDetected(): Unit = {
    val actualVcsRoots = intelliJ.probe.vcsRoots()
    val expectedRoot = VcsRoot("Git", intelliJ.workspace.toRealPath())
    assertEquals(Seq(expectedRoot), actualVcsRoots)
  }
}

object OpenProjectTest extends ConfigFormat {
  object TestData {
    case class SourceRoot(path: Path, kind: SourceFolder.Kind, packagePrefix: Option[String])
    case class Module(name: String, sourceRoots: Set[SourceRoot])
    implicit val contentRootReader: ConfigReader[SourceRoot] = deriveReader[SourceRoot]
    implicit val moduleReader: ConfigReader[Module] = deriveReader[Module]
  }
}
