package org.virtuslab.tests.pants

import java.net.URI
import org.virtuslab.ideprobe.Extensions.URIExtension
import org.virtuslab.ideprobe.dependencies.{Dependency, DependencyProvider, IntelliJDependencyProvider, IntelliJVersion, IntelliJZipResolver, PluginDependencyProvider, PluginResolver, ResourceProvider}
import org.virtuslab.ideprobe.ide.intellij.IntelliJFactory
import org.virtuslab.ideprobe.junit4.IdeProbeTestSuite
import org.virtuslab.ideprobe.pants.{PantsPluginExtension, PantsPluginExtraExtensions}

trait PantsTestSuite
  extends IdeProbeTestSuite
    with PantsPluginExtension
    with PantsPluginExtraExtensions {


  registerFixtureTransformer(f => {
    val dependencies = new DependencyProvider(
      new IntelliJDependencyProvider(Seq({
        version: IntelliJVersion =>
          val name = s"ideaIC-${version.release.getOrElse(version.build)}.portable.zip"
          Dependency.Artifact(URI.create("https://download.jetbrains.com/idea").resolveChild(name))
      },IntelliJZipResolver.Community), ResourceProvider.Default),
      new PluginDependencyProvider(Seq(PluginResolver.Official), ResourceProvider.Default)
    )
    f.copy(intelliJProvider = IntelliJFactory(dependencies, f.intelliJProvider.plugins, f.intelliJProvider.version, f.intelliJProvider.paths, f.intelliJProvider.config))
  })
}
