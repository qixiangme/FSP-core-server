package com.fsp.coreserver.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
open class SecurityConfig {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // User API - ��� �Ұ�
                    .requestMatchers("/users/**").permitAll()

                    // Poem API - ��� ��� (���߿�)
                    .requestMatchers("/poem/**").permitAll()

                    // Community API - ��� ��� (���߿�)
                    .requestMatchers("/communities/**").permitAll()

                    // H2 Console
                    .requestMatchers("/h2-console/**").permitAll()

                    // �������� ���� �ʿ�
                    .anyRequest().authenticated()
            }
            .headers { it.frameOptions { frameOptions -> frameOptions.disable() } }

        return http.build()
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // 모든 origin 허용
        configuration.allowedOriginPatterns = listOf("*")

        // 모든 HTTP Method 허용
        configuration.allowedMethods = listOf("*")

        // 모든 Header 허용
        configuration.allowedHeaders = listOf("*")

        // 인증 정보 허용
        configuration.allowCredentials = true

        // 노출 헤더(선택)
        configuration.exposedHeaders = listOf("Authorization", "X-Total-Count")

        configuration.maxAge = 3600

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }

}
