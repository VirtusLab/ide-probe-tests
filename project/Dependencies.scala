import sbt._

object Dependencies {
  val junit = Seq(
    "junit" % "junit" % "4.12" % Test,
    ("com.novocode" % "junit-interface" % "0.11" % Test).exclude("junit", "junit-dep")
  )
  
  object ideProbe {
    val version = "0.3.0+52-49e9bc63-SNAPSHOT"
    val resolvers = Seq(
      Resolver.sonatypeRepo("public"),
      Resolver.sonatypeRepo("snapshots"),
      MavenRepository(
        "jetbrains-3rd",
        "https://jetbrains.bintray.com/intellij-third-party-dependencies")
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
