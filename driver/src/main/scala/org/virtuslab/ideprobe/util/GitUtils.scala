package org.virtuslab.ideprobe.util

import java.nio.file.Files
import java.nio.file.Path

import org.virtuslab.ideprobe.Shell
import org.virtuslab.ideprobe.dependencies.SourceRepository.Git

object GitUtils {
  def clone(repository: Git): Path = {
    val localRepo = Files.createTempDirectory("scala-plugin-repo")
    val cloned = Shell.run("git", "clone", repository.path.toString, localRepo.toString)
    if (cloned.exitCode != 0) throw new IllegalStateException(s"Could not clone git $repository")
    repository.ref.foreach { ref =>
      val checkout = Shell.run(in = localRepo, "git", "checkout", ref)
      if (checkout.exitCode != 0) throw new IllegalStateException(s"Could not checkout $ref in $repository")
    }
    println(s"Cloned $repository")
    localRepo
  }

  def hash(repository: Git, fallbackRef: String): String = {
    val Ref = repository.ref.getOrElse(fallbackRef)
    val result = Shell.run("git", "ls-remote", repository.path.toString, Ref)

    if (result.exitCode != 0)
      throw new Exception(s"Could not fetch hashes from ${repository.path}. STDERR:\n${result.err}")
    val hash = result.out.linesIterator.map(_.split("\\W+")).collectFirst {
      case Array(hash, Ref) => hash
    }
    hash.orElse(repository.ref).getOrElse(throw new Exception(s"Ref $Ref not found"))
  }
}