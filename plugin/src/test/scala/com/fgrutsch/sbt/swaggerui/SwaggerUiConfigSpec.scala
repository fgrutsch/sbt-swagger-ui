package com.fgrutsch.sbt.swaggerui

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class SwaggerUiConfigSpec extends AnyFunSuite with Matchers {

  test("generates jsObject with optionals beeing empty") {
    val optionalsAndListsEmpty = SwaggerUiConfig.Defaults
    val result                 = optionalsAndListsEmpty.jsObject

    result must include("{")
    result must include("deepLinking: true,")
    result must include("layout: 'StandaloneLayout',")
    result must include("dom_id: '#swagger-ui',")
    result must include("presets: [")
    result must include("SwaggerUIBundle.presets.apis,")
    result must include("SwaggerUIStandalonePreset")
    result must include("],")
    result must include("plugins: [")
    result must include("SwaggerUIBundle.plugins.DownloadUrl")
    result must include("]")
    result must include("}")
  }

  test("generates jsObject with optionals beeing filled") {
    val optionalsAndListsEmpty = SwaggerUiConfig.Defaults
      .withConfigUrl("http://localhost/config.json")
      .enableQueryConfig
      .withUrl("http://localhost/petstore.json")
      .withUrls(
        Seq(
          SwaggerUiConfig.Url.Remote("api-1", "http://localhost/api-1.json"),
          SwaggerUiConfig.Url.Remote("api-2", "../api-2.yaml")
        ))
      .withUrlsPrimaryName("api-2")

    val result = optionalsAndListsEmpty.jsObject

    result must include("{")
    result must include("queryConfigEnabled: true,")
    result must include("configUrl: 'http://localhost/config.json',")
    result must include("url: 'http://localhost/petstore.json',")
    result must include(
      "urls: [{url: 'http://localhost/api-1.json', name: 'api-1'}, {url: '../api-2.yaml', name: 'api-2'}],")
    result must include("'urls.primaryName': 'api-2'")
    result must include("deepLinking: true,")
    result must include("layout: 'StandaloneLayout',")
    result must include("dom_id: '#swagger-ui',")
    result must include("presets: [")
    result must include("SwaggerUIBundle.presets.apis,")
    result must include("SwaggerUIStandalonePreset")
    result must include("],")
    result must include("plugins: [")
    result must include("SwaggerUIBundle.plugins.DownloadUrl")
    result must include("]")
    result must include("}")
  }

}
