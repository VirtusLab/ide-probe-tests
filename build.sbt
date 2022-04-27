

name := "ideprobe-pants"

organization.in(ThisBuild) := "com.twitter.ideprobe"
version.in(ThisBuild) := "0.1"
scalaVersion.in(ThisBuild) := "2.13.1"
resolvers.in(ThisBuild) ++= Dependencies.ideProbe.resolvers
parallelExecution in ThisBuild := false
skip in publish := true

lazy val updateChecker = project
  .in(file("ci/update-checker"))
  .settings(
    name := "update-checker",
    libraryDependencies += "org.jetbrains.intellij" % "plugin-repository-rest-client" % "2.0.15",
    libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1",
    libraryDependencies += Dependencies.ideProbe.pantsDriver,
    resolvers += MavenRepository("intellij-plugin-service", "https://dl.bintray.com/jetbrains/intellij-plugin-service")
  )

lazy val pantsTests = project
  .in(file("tests")).settings(
    name := "pants-tests",
    libraryDependencies += Dependencies.ideProbe.jUnitDriver,
    libraryDependencies += Dependencies.ideProbe.pantsDriver,
    libraryDependencies += Dependencies.ideProbe.bazelDriver,
    libraryDependencies += Dependencies.ideProbe.scalaDriver,
    libraryDependencies ++= Dependencies.junit
  )

lazy val ciSetup = project
  .in(file("ci/setup"))
  .dependsOn(pantsTests % "test->test;compile->compile")
  .settings(
    name := "pants-ci-setup",
    libraryDependencies ++= Dependencies.junit
  )
