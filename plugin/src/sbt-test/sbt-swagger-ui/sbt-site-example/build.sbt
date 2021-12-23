import com.fgrutsch.sbt.swaggerui.SwaggerUiConfig

lazy val root = project
  .in(file("."))
  .enablePlugins(ParadoxSitePlugin, SwaggerUiPlugin)
  .settings(
    organization := "com.fgrutsch",
    name         := "sbt-swagger-ui-test-sbt-site-example",
    scalaVersion := "2.13.7"
  )
  .settings(
    swaggerUiConfig := swaggerUiConfig.value.addUrls(
      SwaggerUiConfig.Url.File(
        "Petstore Api",
        sourceDirectory.value / "main" / "scala" / "docs" / "api" / "petstore.json"
      ),
      SwaggerUiConfig.Url.Remote(
        "Github API",
        "https://api.apis.guru/v2/specs/github.com/1.1.4/openapi.json"
      )
    ),
    makeSite / siteSubdirName := "swagger-ui",
    addMappingsToSiteDir(swaggerUiGenerate / mappings, makeSite / siteSubdirName)
  )
  .settings(
    TaskKey[Unit]("check") := {
      val pluginDirExists = (Compile / target).value / "sbt-swagger-ui"
      if (!pluginDirExists.exists() && !pluginDirExists.isDirectory()) {
        sys.error("target/swagger-ui doesn't exist")
      }

      val sbtSiteSwaggerUiDirExists = (Compile / target).value / "site" / "swagger-ui"
      if (!sbtSiteSwaggerUiDirExists.exists() && !sbtSiteSwaggerUiDirExists.isDirectory()) {
        sys.error("target/site/swagger-ui doesn't exist")
      }

      val petstoreApiExists = sbtSiteSwaggerUiDirExists / "petstore.json"
      if (!petstoreApiExists.isFile()) {
        sys.error("target/site/swagger-ui-test/petstore.json doesn't exist")
      }

      val indexHtmlExists = sbtSiteSwaggerUiDirExists / "index.html"
      if (!indexHtmlExists.isFile()) {
        sys.error("target/site/swagger-ui-test/index.html doesn't exist")
      }
    }
  )
