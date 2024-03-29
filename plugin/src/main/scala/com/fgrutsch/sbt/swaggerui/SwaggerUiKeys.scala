/*
 * Copyright 2023 sbt-swagger-ui contributors
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

import sbt._

trait SwaggerUiKeys {
  val swaggerUiVersion: SettingKey[String] = settingKey("swagger-ui-dist version")
  val swaggerUiDirectory: SettingKey[File] = settingKey("directory to where swagger-ui-dist files get copied")
  val swaggerUiConfig: SettingKey[SwaggerUiConfig] = settingKey(
    "swagger-ui configuration (https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/)")
  val swaggerUiGenerate: TaskKey[Unit] = taskKey("generate swagger-ui site")
}
