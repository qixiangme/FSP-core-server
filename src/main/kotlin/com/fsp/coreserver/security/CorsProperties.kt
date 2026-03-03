package com.fsp.coreserver.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cors")
data class CorsProperties(
    var allowedOrigins: List<String> = emptyList(),
    var allowedOriginPatterns: List<String> = emptyList(),
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS"),
    var allowedHeaders: List<String> = listOf("*"),
    var allowCredentials: Boolean = false
)
