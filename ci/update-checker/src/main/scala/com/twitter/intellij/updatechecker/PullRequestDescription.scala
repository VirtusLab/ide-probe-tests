package com.twitter.intellij.updatechecker

import com.twitter.intellij.updatechecker.UpdateChecker.PluginUpdate
import org.virtuslab.ideprobe.dependencies.IntelliJVersion

object PullRequestDescription {

  def setPrBody(intelliJ: Option[IntelliJVersion], plugins: Iterable[PluginUpdate]): Unit = {
    // See https://github.com/peter-evans/create-pull-request/blob/master/docs/examples.md#setting-the-pull-request-body-from-a-file
    val outputVariable = "pr-body"

    // See https://github.community/t/set-output-truncates-multiline-strings/16852/3
    val lineSeparator = "%0A"

    val header = "This pull request was automatically opened, because updated versions " +
      "of dependencies became available at the Jetbrains Marketplace."

    val lines = Seq(header) ++ intelliJ.map {
      case IntelliJVersion(build, _) => s"* IntelliJ $build"
    } ++ plugins.map {
      case PluginUpdate(id, version) => s"* $id $version"
    }
    println(s"::set-output name=$outputVariable::" + lines.mkString(lineSeparator))
  }

}
