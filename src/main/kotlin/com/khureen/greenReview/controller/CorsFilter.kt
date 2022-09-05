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