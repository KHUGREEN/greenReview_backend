/*
 * Copyright 2022 KHUGREEN (https://github.com/KHUGREEN)
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

package com.khureen.greenReview.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {
    @Override
    fun addCorsMapping(registry: CorsRegistry) {
        registry.addMapping("/**")
    }
}

@Bean
fun corsConfigurationSource() {
    val config = CorsConfiguration()

    config.allowedOriginPatterns = listOf("*")
    config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
    config.allowedHeaders = listOf("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
        "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
        "Cache-Control", "Content-Type")

    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", config)
}