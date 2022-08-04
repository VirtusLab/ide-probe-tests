import sbt._

object Dependencies {
  val junit = Seq(
    "junit" % "junit" % "4.13.2" % Test,
    ("com.novocode" % "junit-interface" % "0.11" % Test).exclude("junit", "junit-dep")
  )
  
  object ideProbe {
    val version = "0.38.0+0-a5cd4cd2+20220727-1604-SNAPSHOT"
    val resolvers = Seq(
      Resolver.sonatypeRepo("public"),
      Resolver.sonatypeRepo("snapshots"),
      MavenRepository(
        "jetbrains-3rd",
        "https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    )

    def apply(name: String): ModuleID = {
      "org.virtuslab.ideprobe" %% name % version
    }

    val jUnitDriver = apply("junit-driver")
    val scalaDriver = apply("scala-probe-driver")
    val bazelDriver = apply("bazel-probe-driver")
    val pantsDriver = apply("pants-probe-driver")
    val robotDriver = apply("robot-driver")
  }

}
