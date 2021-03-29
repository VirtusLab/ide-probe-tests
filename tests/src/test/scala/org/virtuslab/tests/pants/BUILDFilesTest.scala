package org.virtuslab.tests.pants

import java.nio.file.Paths
import org.junit.Test
import org.virtuslab.ideprobe.Assertions
import org.virtuslab.ideprobe.RunningIntelliJFixture
import org.virtuslab.ideprobe.protocol.FileRef
import org.virtuslab.ideprobe.protocol.ProjectRef
import org.virtuslab.ideprobe.protocol.Reference
import org.virtuslab.ideprobe.Extensions._

final class BUILDFilesTest extends PantsTestSuite with Assertions {

  @Test
  def referencesOtherBUILDFilesInBsp(): Unit = {
    checkReferencesToOtherBUILDFiles(openProjectWithBsp(_))
  }

  @Test
  def referencesOtherBUILDFilesInPants(): Unit = {
    checkReferencesToOtherBUILDFiles(openProjectWithPants(_))
  }

  def checkReferencesToOtherBUILDFiles(openProject: RunningIntelliJFixture => ProjectRef): Unit = {
    fixtureFromConfig().run { intelliJ =>
      val project = openProject(intelliJ)

      def buildFile(name: String): FileRef = {
        FileRef(intelliJ.workspace.resolve(name).resolve("BUILD"), project)
      }

      val dirWithBuildFileContainingRefs = intelliJ.config[String]("buildFiles.withReferences")
      val dirsWithReferencedBuildFiles = intelliJ.config[Seq[String]]("buildFiles.referenced")

      val references = intelliJ.probe.fileReferences(buildFile(dirWithBuildFileContainingRefs))
      val expectedReferences = dirsWithReferencedBuildFiles.map { dir =>
        Reference(Paths.get(dir).name, Reference.Target.File(buildFile(dir)))
      }

      assertContains(references)(expectedReferences: _*)
    }
  }

}
