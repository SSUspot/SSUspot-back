package com.ssuspot.sns.infrastructure.security


import com.ssuspot.sns.application.dto.common.JwtTokenDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest

@Component
open class JwtTokenProvider(
    @Value("\${app.jwt.secret}") private val jwtSecret: String,
    @Value("\${app.jwt.accessTokenExpirationMS}") private val accessTokenValidMilSecond: Long = 0,
    @Value("\${app.jwt.refreshTokenExpirationMS}") private val refreshTokenValidMilSecond: Long = 0
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun generateAccessToken(email: String): JwtTokenDto {
        return generateToken(email, accessTokenValidMilSecond)
    }
    fun generateRefreshToken(email: String): JwtTokenDto {
        return generateToken(email, refreshTokenValidMilSecond)
    }

    fun generateToken(email: String, tokenValidMilSecond: Long): JwtTokenDto {
        val now = Date()
        val expiredIn = now.time + tokenValidMilSecond
        val token = Jwts.builder()
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(Date(expiredIn))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()

        return JwtTokenDto(token, expiredIn)
    }

    fun resolveToken(req: HttpServletRequest): Claims? {
        var token = req.getHeader("Authorization")
        token = when {
            token == null -> return null
            token.contains("Bearer") -> token.replace(
                "Bearer ",
                ""
            )
            else -> throw DecodingException("")
        }
        return getClaimsFromToken(token)
    }

    fun getClaimsFromToken(token: String): Claims? {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
    }

    fun getAuthentication(claims: Claims): Authentication {
        return UsernamePasswordAuthenticationToken(claims["email"], null, null)
    }

    fun getUserEmailFromToken(token: String): String {
        val resolvedToken = if( token.contains("Bearer")) {token.replace(
            "Bearer ",
            ""
        )} else token
        println("resolvedToken: $resolvedToken")
        return getClaimsFromToken(resolvedToken)!!["email"] as String
    }
}