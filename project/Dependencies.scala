import sbt._

object Dependencies {

  val plugin: Seq[ModuleID] = Seq(
    "org.jsoup"      % "jsoup"     % "1.14.3",
    "org.scalatest" %% "scalatest" % "3.2.12" % Test
  )

  val organizeimports: ModuleID = "com.github.liancheng" %% "organize-imports" % "0.6.0"
  val sbtWebPlugin: ModuleID    = "com.typesafe.sbt"      % "sbt-web"          % "1.4.4"

}
