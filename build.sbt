import java.time.LocalDate

ThisBuild / scalafixDependencies += Dependencies.organizeimports
ThisBuild / scalaVersion := "2.13.12"

addCommandAlias("codeFmt", ";headerCreate;scalafmtAll;scalafmtSbt;scalafixAll")
addCommandAlias("codeVerify", ";scalafmtCheckAll;scalafmtSbtCheck;scalafixAll --check;headerCheck")

lazy val commonSettings = Seq(
  organization           := "com.fgrutsch",
  sonatypeCredentialHost := "s01.oss.sonatype.org",
  sonatypeRepository     := "https://s01.oss.sonatype.org/service/local",
  sonatypeProfileName    := "com.fgrutsch",
  startYear              := Some(2021),
  homepage               := Some(url("https://github.com/fgrutsch/sbt-swagger-ui")),
  licenses               := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  scmInfo := Some(
    ScmInfo(homepage.value.get, "scm:git:https://github.com/fgrutsch/sbt-swagger-ui.git")
  ),
  developers += Developer(
    "contributors",
    "Contributors",
    "",
    url("https://github.com/fgrutsch/sbt-swagger-ui/graphs/contributors")
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "utf-8",
    "-explaintypes",
    "-feature",
    "-unchecked",
    "-Xcheckinit",
    "-Xfatal-warnings",
    "-Ywarn-dead-code",
    "-Ywarn-unused"
  ),
  headerLicense     := Some(HeaderLicense.ALv2(LocalDate.now.getYear.toString, "sbt-swagger-ui contributors")),
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(
    publish / skip := true
  )
  .aggregate(plugin)

lazy val plugin = project
  .in(file("plugin"))
  .settings(commonSettings)
  .settings(
    name := "sbt-swagger-ui",
    libraryDependencies ++= Dependencies.plugin,
    addSbtPlugin(Dependencies.sbtWebPlugin),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )
  .enablePlugins(SbtPlugin)
