package com.ssuspot.sns.security

import com.ssuspot.sns.core.common.model.dto.JwtTokenDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.Keys
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

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
        val email = claims["email"] as String
        val userPrincipal = UserPrincipal(email)
        return UsernamePasswordAuthenticationToken(userPrincipal, "", listOf()) // 권한 설정
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