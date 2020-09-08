package org.virtuslab.ideprobe.pants

import java.io.InputStream

import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.Id
import org.virtuslab.ideprobe.Shell
import org.virtuslab.ideprobe.dependencies.DependencyBuilder
import org.virtuslab.ideprobe.dependencies.Resource
import org.virtuslab.ideprobe.dependencies.ResourceProvider
import org.virtuslab.ideprobe.dependencies.SourceRepository
import org.virtuslab.ideprobe.dependencies.SourceRepository.Git
import org.virtuslab.ideprobe.util.GitUtils

object PantsPluginBuilder extends DependencyBuilder(Id("pants")) {
  def build(repository: SourceRepository, resources: ResourceProvider): Resource =
    repository match {
      case git: Git =>
        val hash = GitUtils.hash(git, "HEAD")
        val artifact = git.path.resolveChild(hash)

        resources.get(artifact, provider = () => build(git, resources))
    }

  private def build(repository: Git, resources: ResourceProvider): InputStream = {
    val localRepo = GitUtils.clone(repository)

    val command0 = List("./scripts/setup-ci-environment.sh")
    val result0 = Shell.run(localRepo, Map.empty[String, String], command0: _*)
    if (result0.exitCode != 0) throw new Exception(s"Couldn't set up ci environment. STDERR:\n${result0.err}")

    val env = Map("TRAVIS_BRANCH" -> "master")
    val command = List("./scripts/deploy/deploy.sh", "--skip-publish")
    val result = Shell.run(localRepo, env, command: _*)
    if (result.exitCode != 0) throw new Exception(s"Couldn't build pants plugin. STDERR:\n${result.err}")
    println("Built pants plugin")
    val files = localRepo.toFile.listFiles()
    files.foreach(println)
    val output = files.find(_.getName.matches("pants_.*\\.zip")).get
    output.renameTo(new java.io.File("pants.zip"))
    localRepo.resolve("pants.zip").inputStream
  }

}
