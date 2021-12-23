import com.fgrutsch.sbt.swaggerui.SwaggerUiConfig

lazy val root = project
  .in(file("."))
  .enablePlugins(SwaggerUiPlugin)
  .settings(
    organization := "com.fgrutsch",
    name         := "sbt-swagger-ui-test-simple",
    scalaVersion := "2.13.7"
  )
  .settings(
    swaggerUiDirectory := (Compile / target).value / "swagger-ui-test",
    swaggerUiConfig := SwaggerUiConfig.Defaults
      .addUrls(
        SwaggerUiConfig.Url.File("PetstoreApi", baseDirectory.value / "docs" / "api" / "petstore.json")
      )
  )
  .settings(
    TaskKey[Unit]("check") := {
      val dirExists = (Compile / target).value / "swagger-ui-test"
      if (!dirExists.exists() && !dirExists.isDirectory()) {
        sys.error("target/swagger-ui-test doesn't exist")
      }

      val petstoreApiExists = dirExists / "petstore.json"
      if (!petstoreApiExists.isFile()) {
        sys.error("target/swagger-ui-test/petstore.json doesn't exist")
      }

      val indexHtml        = dirExists / "index.html"
      val indexHtmlContent = IO.read(indexHtml)

      if (!indexHtmlContent.contains("urls: [{url: 'petstore.json', name: 'PetstoreApi'}]")) {
        sys.error("index.html doesn't contain 'urls: [{url: 'petstore.json', name: 'PetstoreApi'}]'")
      }
    }
  )
