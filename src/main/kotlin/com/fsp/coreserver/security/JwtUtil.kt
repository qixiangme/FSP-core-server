package com.fsp.coreserver.security
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey


@Component
class JwtUtil(
    @Value("\${jwt.secret}") jwtSecret: String,
    @Value("\${jwt.access-token-validity-ms:3600000}") private val accessTokenValidity: Long,
    @Value("\${jwt.refresh-token-validity-ms:86400000}") private val refreshTokenValidity: Long

) {
    private val secretKey : SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
    fun generateToken(email: String) : String  =
        Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(secretKey,SignatureAlgorithm.HS256)
            .compact()

    fun generateRefreshToken(email: String) : String =
        Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + refreshTokenValidity))
            .signWith(secretKey,SignatureAlgorithm.HS256)
            .compact()

    private fun getClaims(token: String) : Claims =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
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
}