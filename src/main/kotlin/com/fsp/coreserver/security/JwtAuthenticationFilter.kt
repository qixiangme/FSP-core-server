package com.fsp.coreserver.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        // ✅ 모든 요청에 대해 필터 적용 안함 (개발용)
        return true

        // 또는 특정 경로만 제외
        // val path = request.requestURI
        // return path.startsWith("/users/") ||
        //        path.startsWith("/poem/") ||
        //        path.startsWith("/communities/") ||
        //        path.startsWith("/h2-console")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            if (jwtUtil.validateToken(token)) {
                val email = jwtUtil.getEmail(token)
                val authentication = UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    emptyList()
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        filterChain.doFilter(request, response)
    }
}