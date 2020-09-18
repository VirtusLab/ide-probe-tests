package org.virtuslab.ideprobe.pants

import java.io.InputStream
import java.nio.file.Paths

import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.Id
import org.virtuslab.ideprobe.ProbeExtensions
import org.virtuslab.ideprobe.Shell
import org.virtuslab.ideprobe.dependencies.DependencyBuilder
import org.virtuslab.ideprobe.dependencies.Resource
import org.virtuslab.ideprobe.dependencies.ResourceProvider
import org.virtuslab.ideprobe.dependencies.SourceRepository
import org.virtuslab.ideprobe.dependencies.SourceRepository.Git
import org.virtuslab.ideprobe.util.GitUtils

object PantsPluginBuilder extends DependencyBuilder(Id("pants")) with ProbeExtensions {
  def build(repository: SourceRepository, resources: ResourceProvider): Resource =
    repository match {
      case git: Git =>
        val hash = GitUtils.hash(git, "HEAD")
        val artifact = git.path.resolveChild(hash)

        resources.get(artifact, provider = () => build(git))
    }

  private def build(repository: Git): InputStream = {
    val localRepo = GitUtils.clone(repository)

    val setupCiEnvironment = List("./scripts/setup-ci-environment.sh")
    val result0 = Shell.run(localRepo, setupCiEnvironment: _*)
    if (result0.exitCode != 0) throw new Exception(s"Couldn't set up ci environment. STDERR:\n${result0.err}")

    val env = Map("TRAVIS_BRANCH" -> "master")
    val deploy = List("./scripts/deploy/deploy.sh", "--skip-publish")
    val result = Shell.run(localRepo, env, deploy: _*)
    if (result.exitCode != 0) throw new Exception(s"Couldn't build pants plugin. STDERR:\n${result.err}")

    val files = localRepo.toFile.listFiles()
    val output = files.find(_.getName.matches("pants_.*\\.zip")).getOrElse {
      throw new Exception(s"Couldn't find pants archive. Existing files:\n${files.mkString("\n")}")
    }

    val pantsPath = Paths.get("/tmp", "pants.zip")
    output.renameTo(pantsPath.toFile)
    if(java.nio.file.Files.exists(pantsPath)) {
      println(s"Built pants at $pantsPath")
      localRepo.delete()
      pantsPath.inputStream
    } else throw new Exception(s"Could not move $output to $pantsPath")
  }

}
