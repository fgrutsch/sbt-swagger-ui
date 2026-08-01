import sbt._

object Dependencies {

  val plugin: Seq[ModuleID] = Seq(
    "org.jsoup"      % "jsoup"     % "1.23.1",
    "org.scalatest" %% "scalatest" % "3.2.20" % Test
  )

  val sbtWebPlugin: ModuleID = "com.github.sbt" % "sbt-web" % "1.5.8"

}
