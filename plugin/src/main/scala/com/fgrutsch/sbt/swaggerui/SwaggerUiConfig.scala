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

object SwaggerUiConfig {

  /**
   * Default SwaggerUiConfig configuration settings
   */
  val Defaults: SwaggerUiConfig = SwaggerUiConfig(
    deepLinking = true,
    layout = "StandaloneLayout",
    configUrl = None,
    queryConfigEnabled = None,
    url = None,
    urls = Seq.empty,
    urlsPrimaryName = None
  )

  sealed abstract class Url(val name: String)
  object Url {
    final case class Remote(override val name: String, value: String) extends Url(name)
    final case class File(override val name: String, value: sbt.File) extends Url(name)
  }
}

/**
 * Describes swagger-ui configuration as documented here:
 * https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/
 */
final case class SwaggerUiConfig(
    deepLinking: Boolean,
    layout: String,
    queryConfigEnabled: Option[Boolean],
    configUrl: Option[String],
    url: Option[String],
    urls: Seq[SwaggerUiConfig.Url],
    urlsPrimaryName: Option[String]
) {

  def enableDeepLinking                         = copy(deepLinking = true)
  def disableDeepLinking                        = copy(deepLinking = false)
  def withLayout(value: String)                 = copy(layout = value)
  def enableQueryConfig                         = copy(queryConfigEnabled = Some(true))
  def disableQueryConfig                        = copy(queryConfigEnabled = Some(false))
  def withConfigUrl(value: String)              = copy(configUrl = Some(value))
  def withUrl(value: String)                    = copy(url = Some(value))
  def withUrls(value: Seq[SwaggerUiConfig.Url]) = copy(urls = value)
  def addUrls(value: SwaggerUiConfig.Url*)      = copy(urls = urls ++ value)
  def withUrlsPrimaryName(value: String)        = copy(urlsPrimaryName = Some(value))

  def jsObject: String = {
    val queryConfigEnabledJsKeyValue = queryConfigEnabled match {
      case Some(value) => s"queryConfigEnabled: $value,"
      case None        => ""
    }

    val configUrlJsKeyValue = configUrl match {
      case Some(value) => s"configUrl: '$value',"
      case None        => ""
    }

    val urlJsKeyValue = url match {
      case Some(value) => s"url: '$value',"
      case None        => ""
    }

    val urlsJsKeyValues = urls match {
      case Nil => ""
      case values =>
        val keyValueObjects = values.map {
          case u: SwaggerUiConfig.Url.Remote => s"{url: '${u.value}', name: '${u.name}'}"
          case u: SwaggerUiConfig.Url.File   => s"{url: '${u.value.getName}', name: '${u.name}'}"
        }
        s"urls: [${keyValueObjects.mkString(", ")}],",
    }

    val urlsPrimaryNameJsKeyValues = urlsPrimaryName match {
      case Some(value) => s"'urls.primaryName': '$value',"
      case None        => ""
    }

    s"""
    |{
    |   $queryConfigEnabledJsKeyValue
    |   $configUrlJsKeyValue
    |   $urlJsKeyValue
    |   $urlsJsKeyValues
    |   $urlsPrimaryNameJsKeyValues
    |   deepLinking: $deepLinking,
    |   layout: '$layout',
    |   dom_id: '#swagger-ui',
    |   presets: [
    |     SwaggerUIBundle.presets.apis,
    |     SwaggerUIStandalonePreset  
    |   ],
    |   plugins: [
    |     SwaggerUIBundle.plugins.DownloadUrl
    |   ]
    |}
    """.stripMargin
  }

}
