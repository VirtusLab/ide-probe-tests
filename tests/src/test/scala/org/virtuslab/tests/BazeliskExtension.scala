package org.virtuslab.tests

import java.io.{File, FileInputStream}
import java.net.URL
import java.nio.file.Path
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.SystemUtils
import org.virtuslab.bazelprobe.driver.BazelPluginExtension
import org.virtuslab.ideprobe.{Config, IdeProbeFixture, IntelliJFixture, OS}

trait BazeliskExtension extends IdeProbeFixture with BazelPluginExtension{
  registerFixtureTransformer(_.withAfterWorkspaceSetup((ij: IntelliJFixture, path: Path) => {
    val c = ij.config
    installBazelisk(bazelPath(path), ij.config)
  }))

  def bazelPath(path: Path): Path = {
    path.resolve("bin/bazel")
  }

  private def downloadFile(url: URL, filePath: Path, expectedChecksum: String): Unit = {
    val file: File = filePath.toFile
    FileUtils.copyURLToFile(
      url,
      file
    )
    val checksum = DigestUtils.sha256Hex(new FileInputStream(file))
    if (checksum != expectedChecksum) {
      throw new RuntimeException(s"Invalid checksum for '$url', expected: $expectedChecksum, actual: $checksum")
    }
  }

  private def installBazelisk(bazelExecPath: Path, config: Config): Path = {
    import org.virtuslab.ideprobe.Extensions.PathExtension
    val osKey = OS.Current match {
      case OS.Windows => throw new RuntimeException("We do not support Windows")
      case OS.Unix => "linux"
      case OS.Mac => "mac"
    }
    val (expectedChecksum, url) = (
      config[String](s"bazelisk.$osKey.sha256sum"),
      config[String](s"bazelisk.$osKey.url")
    )
    downloadFile(new URL(url), bazelExecPath, expectedChecksum)
    bazelExecPath.makeExecutable()
    bazelExecPath
  }
}
