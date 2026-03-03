package com.fsp.coreserver.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val issuer: String,
    val audience: String,
    val accessTokenValidity: Long,
    val refreshTokenValidity: Long
)
