import sbt._
import sbt.Keys._
import sbtghactions.GenerativePlugin
import sbtghactions.GenerativePlugin.autoImport._
import sbtghactions.WorkflowStep._

object SetupGithubActionsPlugin extends AutoPlugin {

  override def requires: Plugins = GenerativePlugin
  override def trigger           = allRequirements
  override def buildSettings: Seq[Setting[_]] = Seq(
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowJavaVersions += JavaSpec.temurin("17"),
    githubWorkflowBuild   := Seq(WorkflowStep.Sbt(List("codeVerify", "test", "scripted"))),
    githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"))),
    githubWorkflowPublishTargetBranches += RefPredicate.StartsWith(Ref.Tag("v")),
    githubWorkflowPublish := Seq(
      WorkflowStep.Sbt(
        List("ci-release"),
        env = Map(
          "PGP_PASSPHRASE"    -> "${{ secrets.PGP_PASSPHRASE }}",
          "PGP_SECRET"        -> "${{ secrets.PGP_SECRET }}",
          "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
          "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
        )
      )
    )
  )

}
