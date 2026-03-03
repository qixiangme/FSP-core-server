package com.fsp.coreserver.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Duration
import java.util.Date


@Component
class JwtUtil(
    private val jwtProperties: JwtProperties,
    private val redisTemplate: StringRedisTemplate
) {
    private val secretKey: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret))
    private val accessTokenValidity = jwtProperties.accessTokenValidity
    private val refreshTokenValidity = jwtProperties.refreshTokenValidity

    fun generateToken(email: String) : String  =
        Jwts.builder()
            .setSubject(email)
            .setIssuer(jwtProperties.issuer)
            .setAudience(jwtProperties.audience)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(SignatureAlgorithm.HS256,secretKey)
            .compact()

    fun generateRefreshToken(email: String) : String =
        Jwts.builder()
            .setSubject(email)
            .setIssuer(jwtProperties.issuer)
            .setAudience(jwtProperties.audience)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + refreshTokenValidity))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    fun storeRefreshToken(userId: Long, token: String) {
        val key = refreshTokenKey(userId)
        redisTemplate.opsForValue()
            .set(key, token, Duration.ofMillis(refreshTokenValidity))
    }

    fun validateRefreshToken(userId: Long, token: String): Boolean {
        if (!validateToken(token)) {
            return false
        }
        val storedToken = redisTemplate.opsForValue().get(refreshTokenKey(userId))
        return storedToken == token
    }

    fun revokeRefreshToken(userId: Long) {
        redisTemplate.delete(refreshTokenKey(userId))
    }

    private fun getClaims(token: String) : Claims =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .requireIssuer(jwtProperties.issuer)
            .requireAudience(jwtProperties.audience)
            .build()
            .parseClaimsJws(token)
            .body
    fun getEmail(token:String) : String =
        getClaims(token).subject
    fun validateToken(token: String): Boolean{
        return try{
            val claims = getClaims(token)
            !claims.expiration.before(Date())
        }
        catch (e: Exception){
            false
        }
    }

    private fun refreshTokenKey(userId: Long): String = "refresh:$userId"
}
