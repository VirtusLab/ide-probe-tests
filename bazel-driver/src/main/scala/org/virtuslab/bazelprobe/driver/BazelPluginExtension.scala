package org.virtuslab.bazelprobe.driver

import org.virtuslab.ideprobe.dependencies.Plugin
import org.virtuslab.ideprobe.{IdeProbeFixture, ProbeDriver}

trait BazelPluginExtension extends IdeProbeFixture {

  val bazelProbePlugin: Plugin = Plugin.Bundled(s"ideprobe-bazel-${BuildInfo.version}.zip")

  registerFixtureTransformer(_.withPlugin(bazelProbePlugin))

  implicit def bazelProbeDriver(driver: ProbeDriver): BazelProbeDriver = new BazelProbeDriver(driver)


}
