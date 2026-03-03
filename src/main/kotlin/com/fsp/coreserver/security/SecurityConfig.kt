package com.fsp.coreserver.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CorsProperties::class)
open class SecurityConfig(
    private val corsProperties: CorsProperties
) {

    @Bean
    open fun filterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .cors {  }
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // User API - 인증 불필요
                    .requestMatchers("/users/**").permitAll()
                    .requestMatchers("/conversations/elaborate/**").permitAll()
//                    // Poem API - 모두 허용 (개발용)
//                    .requestMatchers("/poem/**").permitAll()
//
//                    // Community API - 모두 허용 (개발용)
//                    .requestMatchers("/communities/**").permitAll()
//
//                    // H2 Console
//                    .requestMatchers("/h2-console/**").permitAll()

                    // 나머지는 인증 필요
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter::class.java)
            .headers { it.frameOptions { frameOptions -> frameOptions.disable() } }

        return http.build()
    }
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedMethods = corsProperties.allowedMethods
        config.allowedHeaders = corsProperties.allowedHeaders
        config.allowCredentials = corsProperties.allowCredentials
        if (corsProperties.allowCredentials && corsProperties.allowedOrigins.any { it == "*" }) {
            throw IllegalArgumentException("CORS allowCredentials=true does not support wildcard origins.")
        }
        if (corsProperties.allowedOrigins.isNotEmpty()) {
            config.allowedOrigins = corsProperties.allowedOrigins
        } else if (corsProperties.allowedOriginPatterns.isNotEmpty()) {
            config.allowedOriginPatterns = corsProperties.allowedOriginPatterns
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
