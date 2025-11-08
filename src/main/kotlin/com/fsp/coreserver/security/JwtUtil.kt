package com.fsp.coreserver.security
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date


@Component
class JwtUtil {
    private val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val accessTokenValidity = 1000 * 60 * 60L
    private val refreshTokenValidity = 1000 * 60 * 60 * 24L

    fun generateToken(email: String) : String  =
        Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(SignatureAlgorithm.HS256,secretKey)
            .compact()

    fun generateRefreshToken(email: String) : String =
        Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + refreshTokenValidity))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    private fun getClaims(token: String) : Claims =
        Jwts.parser()
            .setSigningKey(secretKey)
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