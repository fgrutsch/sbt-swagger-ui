[![Maven](https://maven-badges.herokuapp.com/maven-central/com.fgrutsch/sbt-swagger-ui/badge.png?style=for-the-badge)](https://search.maven.org/search?q=g:%22com.fgrutsch%22%20AND%20a:%22sbt-swagger-ui%22)
[![Github Actions CI Workflow](https://img.shields.io/github/actions/workflow/status/fgrutsch/sbt-swagger-ui/ci.yml?logo=Github&style=for-the-badge)](https://github.com/fgrutsch/sbt-swagger-ui/actions/workflows/ci.yml?query=branch%3Amain)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

# sbt-swagger-ui

SBT plugin that generates a swagger-ui site for static website hosting.

## Getting Started

Add the following to your `project/plugins.sbt`:

```scala
addSbtPlugin("com.fgrutsch" % "sbt-swagger-ui" % "x.x.x")
```

After that you just need to enable the plugin on your SBT project and configure the OpenAPIs you want to serve:

```scala
import com.fgrutsch.sbt.swaggerui.SwaggerUiConfig

lazy val docs = project
  .in(file("docs"))
  .enablePlugins(SwaggerUiPlugin)
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
    )
  )
```

This would add two OpenAPIs to the swagger-ui site, one that is served from a local file (`Petstore API`) and one from a remote location (`Github API`).

Check `com.fgrutsch.sbt.swaggerui.SwaggerUiConfig` for a full set of configuration values.

To generate the swagger-ui site you just need to run `sbt docs/swaggerUiGenerate` and check the output in the directory you configured (`swaggerUiDirectory`).

### sbt-site integration

You might already use [sbt-site](https://github.com/sbt/sbt-site) to generate a static documentation website. You can easily integrate `sbt-swagger-ui` by mapping the output of sbt-swagger-ui to sbt-site's directory:

```scala
import com.fgrutsch.sbt.swaggerui.SwaggerUiConfig

lazy val docs = project
  .in(file("docs"))
  .enablePlugins(SwaggerUiPlugin, SitePlugin)
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
```

Now if you run `sbt docs/makeSite` (or `sbt docs/previewSite` to preview) the swagger-ui site will be added to the sbt-site's output directory.

For example you could link to the swagger-ui site like this:

```html
<html>
    <body>
        <h1>API</h1>
        <p>Please check the swagger-ui documentation <a href="swagger-ui/index.html">here</a>.</p>
    </body>
</html>
```

## Settings and Tasks

| Name               | Type    | Description                                                                                          | Default Value                                           |
|--------------------|---------|------------------------------------------------------------------------------------------------------|---------------------------------------------------------|
| swaggerUiVersion   | Setting | The WebJar swagger-ui-dist version to use                                                            | `4.1.3`                                                 |
| swaggerUiDirectory | Setting | Directory to where swagger-ui-dist files get copied                                                  | `(Compile / target).value / "sbt-swagger-ui"`           |
| swaggerUiConfig    | Setting | swagger-ui configuration (https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/) | `com.fgrutsch.sbt.swaggerui.SwaggerUiConfig.Defaults`   |
| swaggerUiGenerate  | Task    | generate swagger-ui site                                                                             | `-`                                                     |

## Contributors

* [Fabian Grutsch](https://github.com/fgrutsch)

## License

This code is licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.txt).