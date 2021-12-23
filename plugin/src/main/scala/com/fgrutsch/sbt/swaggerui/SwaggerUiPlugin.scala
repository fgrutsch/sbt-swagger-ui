/*
 * Copyright 2021 sbt-swagger-ui contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fgrutsch.sbt.swaggerui

import com.typesafe.sbt.web.Import.WebKeys._
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb
import org.jsoup.Jsoup
import sbt.Keys._
import sbt._

object SwaggerUiPlugin extends AutoPlugin {

  private val swaggerUiWebjarIdentifier = "lib/swagger-ui-dist"

  override def requires: Plugins = SbtWeb

  override def trigger = allRequirements

  object autoImport extends SwaggerUiKeys
  import autoImport._

  override lazy val projectSettings = Seq(
    swaggerUiVersion   := "4.1.3",
    swaggerUiDirectory := (Compile / target).value / "sbt-swagger-ui",
    swaggerUiConfig    := SwaggerUiConfig.Defaults,
    swaggerUiGenerate := {
      // use TestAssets because for some reason swagger-ui-dist webjar is not in mappings when used together with
      // https://www.scala-sbt.org/sbt-site/generators/paradox.html
      val swaggerUiWebjarDir = (TestAssets / assets / mappings).value
        .collectFirst {
          case (file, path) if path == swaggerUiWebjarIdentifier => file
        }
        .getOrElse(throw new IllegalStateException("Expected swagger-ui-dist webjar to be present on classpath."))

      val filter = "*.html" | "*.css" | "*.js" | "*.png"
      val swaggerUiMappings = IO.listFiles(swaggerUiWebjarDir, filter).toSeq.map { f =>
        f -> swaggerUiDirectory.value / f.getName
      }

      val openApiMappings = swaggerUiConfig.value.urls
        .collect { case f: SwaggerUiConfig.Url.File =>
          f.value -> swaggerUiDirectory.value / f.value.getName
        }

      IO.copy(swaggerUiMappings ++ openApiMappings)

      val indexHtml         = swaggerUiDirectory.value / "index.html"
      val indexHtmlDocument = Jsoup.parse(IO.read(indexHtml))

      val defaultSwaggerUiScript = indexHtmlDocument.select("body > script").last()

      val configuredSwaggerUiScript = s"""
        |window.onload = function() {
        |   const ui = SwaggerUIBundle(${swaggerUiConfig.value.jsObject});
        |   window.ui = ui;
        |};
        """.stripMargin
      defaultSwaggerUiScript.html(configuredSwaggerUiScript)

      IO.write(indexHtml, indexHtmlDocument.toString)
    },
    swaggerUiGenerate / mappings := {
      Def
        .sequential(
          swaggerUiGenerate,
          Def.task(IO.listFiles(swaggerUiDirectory.value).toSeq.map(f => f -> f.getName))
        )
        .value
    },
    libraryDependencies ++= Seq(
      "org.webjars.npm" % "swagger-ui-dist" % swaggerUiVersion.value % Provided
    )
  )

}
