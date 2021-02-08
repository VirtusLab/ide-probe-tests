name := "ideprobe-pants"

organization.in(ThisBuild) := "com.twitter.ideprobe"
version.in(ThisBuild) := "0.1"
scalaVersion.in(ThisBuild) := "2.13.1"
intellijBuild.in(ThisBuild) := "203.6682.115"
resolvers.in(ThisBuild) ++= Dependencies.ideProbe.resolvers
skip in publish := true

import IdeaPluginAdapter._
import IdeaPluginDevelopment._

/**
 * By default, the sbt-idea-plugin gets applied to all of the projects.
 * We want it only in the plugin projects, so we need to disable it here
 * as well as for each created project separately.
 */
disableIdeaPluginDevelopment()

val pluginSettings = Seq(
  packageMethod := PackagingMethod.Standalone(),
  intellijPlugins ++= Seq(
    "com.intellij.java".toPlugin,
    "JUnit".toPlugin,
    "PythonCore".toPlugin,
    "org.intellij.scala".toPlugin,
  )
)

lazy val pantsProbeApi = project
  .in(file("api"))
  .settings(
    name := "pants-probe-api",
    libraryDependencies += Dependencies.ideProbe.api
  )

lazy val bazelProbeApi = project
  .in(file("bazel-probe-api"))
  .settings(
    name := "bazel-probe-api",
    libraryDependencies += Dependencies.ideProbe.api
  )

lazy val pantsProbePlugin = ideaPlugin("probePlugin", id = "pantsProbePlugin")
  .dependsOn(pantsProbeApi)
  .settings(
    libraryDependencies += Dependencies.ideProbe.probePlugin,
    intellijPluginName := "ideprobe-pants",
    packageArtifactZipFilter := { file: File =>
      // We want only this main jar to be packaged, all the library dependencies
      // are already in the probePlugin which will be available in runtime as we
      // depend on it in plugin.xml.
      // The packaging plugin is created to support one plugin per build, so there
      // seems to be no way to prevent including probePlugin.jar in the dist reasonable way.
      file.getName == "pantsProbePlugin.jar"
    },
    intellijPlugins += "com.intellij.plugins.pants:1.15.1.42d84c497b639ef81ebdae8328401e3966588b2c:bleedingedge".toPlugin,
    name := "pants-probe-plugin"
  )

lazy val bazelProbePlugin = ideaPlugin("probePluginBazel", id = "bazelProbePlugin")
  .dependsOn(bazelProbeApi)
  .settings(
    libraryDependencies += Dependencies.ideProbe.probePlugin,
    intellijPluginName := "ideprobe-bazel",
    packageArtifactZipFilter := { file: File =>
      // We want only this main jar to be packaged, all the library dependencies
      // are already in the probePlugin which will be available in runtime as we
      // depend on it in plugin.xml.
      // The packaging plugin is created to support one plugin per build, so there
      // seems to be no way to prevent including probePlugin.jar in the dist reasonable way.
      file.getName == "bazelProbePlugin.jar"
    },
    intellijPlugins += "com.google.idea.bazel.ijwb:2020.12.01.0.1".toPlugin,
    name := "bazel-probe-plugin"
  )

lazy val pantsProbeDriver = project
  .in(file("driver"))
  .enablePlugins(BuildInfoPlugin)
  .disableIdeaPluginDevelopment
  .usesIdeaPlugin(pantsProbePlugin)
  .settings(
    name := "pants-probe-driver",
    libraryDependencies += Dependencies.ideProbe.driver,
    libraryDependencies += Dependencies.ideProbe.robotDriver,
    libraryDependencies += Dependencies.ideProbe.probeScalaDriver,
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "com.twitter.intellij.pants"
  )

lazy val bazelProbeDriver = project
  .in(file("bazel-driver"))
  .enablePlugins(BuildInfoPlugin)
  .disableIdeaPluginDevelopment
  .usesIdeaPlugin(bazelProbePlugin)
  .settings(
    name := "bazel-probe-driver",
    libraryDependencies += Dependencies.ideProbe.driver,
    libraryDependencies += Dependencies.ideProbe.robotDriver,
    libraryDependencies += Dependencies.ideProbe.probeScalaDriver,
    libraryDependencies += "commons-codec" % "commons-codec" % "1.15",
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "org.virtuslab.ideprobe.bazel"
  )

lazy val updateChecker = project
  .in(file("ci/update-checker"))
  .disableIdeaPluginDevelopment
  .dependsOn(pantsProbeDriver)
  .settings(
    name := "update-checker",
    libraryDependencies += "org.jetbrains.intellij" % "plugin-repository-rest-client" % "2.0.15",
    libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1",
    resolvers += MavenRepository("intellij-plugin-service", "https://dl.bintray.com/jetbrains/intellij-plugin-service")
  )

lazy val pantsTests = project
  .in(file("tests"))
  .disableIdeaPluginDevelopment
  .dependsOn(pantsProbeDriver)
  .dependsOn(bazelProbeDriver)
  .settings(
    name := "pants-tests",
    libraryDependencies += Dependencies.ideProbe.jUnitDriver,
    libraryDependencies ++= Dependencies.junit
  )

lazy val ciSetup = project
  .in(file("ci/setup"))
  .disableIdeaPluginDevelopment
  .dependsOn(pantsTests % "test->test")
  .settings(
    name := "pants-ci-setup",
    libraryDependencies ++= Dependencies.junit
  )

def ideaPlugin(path: String, id: String = null) = {
  val resolvedId = Option(id).getOrElse(path)
  Project(resolvedId, file(path))
    .enableIdeaPluginDevelopment
    .settings(pluginSettings)
}
