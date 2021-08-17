name := "ideprobe-gradle"

organization.in(ThisBuild) := "com.twitter.ideprobe"
version.in(ThisBuild) := "0.1"
scalaVersion.in(ThisBuild) := "2.13.1"
resolvers.in(ThisBuild) ++= Dependencies.ideProbe.resolvers
parallelExecution in ThisBuild := false
skip in publish := true

lazy val pantsTests = project
  .in(file("tests")).settings(
    name := "pants-tests",
    libraryDependencies += Dependencies.ideProbe.driver,
    libraryDependencies += Dependencies.ideProbe.robotDriver,
    fork in run := true
  )
