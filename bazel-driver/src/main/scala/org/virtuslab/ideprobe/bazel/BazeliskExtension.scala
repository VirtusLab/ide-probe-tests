package org.virtuslab.ideprobe.bazel

import java.net.URL
import java.nio.file.{Files, Path}
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.virtuslab.ideprobe.Extensions._
import org.virtuslab.ideprobe.{Config, OS, error}

trait BazeliskExtension {

  protected def installBazelisk(bazelExecPath: Path, config: Config): Unit = {
    val osKey = OS.Current match {
      case OS.Unix => "linux"
      case OS.Mac => "mac"
      case other => error(s"Unsupported os: $other")
    }
    val expectedChecksum = config[String](s"bazelisk.$osKey.sha256sum")
    val url = config[String](s"bazelisk.$osKey.url")

    downloadFile(new URL(url), bazelExecPath, expectedChecksum)
    bazelExecPath.makeExecutable()
  }

  protected def bazelPath(path: Path): Path = {
    path.resolve("bin/bazel")
  }

  private def downloadFile(url: URL, path: Path, expectedChecksum: String): Unit = {
    FileUtils.copyURLToFile(url, path.toFile)
    val checksum = DigestUtils.sha256Hex(Files.newInputStream(path))
    if (checksum != expectedChecksum) {
      error(s"Invalid checksum for '$url', expected: $expectedChecksum, actual: $checksum")
    }
  }
}
