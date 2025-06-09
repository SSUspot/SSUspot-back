package com.ssuspot.sns.support.helper

import com.ssuspot.sns.application.dto.common.JwtTokenDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import java.util.*
import javax.crypto.SecretKey

object JwtTestHelper {
    
    private const val TEST_SECRET = "test-secret-key-for-testing-purpose-only-do-not-use-in-production"
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(TEST_SECRET.toByteArray())
    
    fun generateTestToken(
        email: String = "test@example.com",
        expirationMs: Long = 3600000L // 1 hour
    ): String {
        val now = Date()
        val expiredAt = Date(now.time + expirationMs)
        
        return Jwts.builder()
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun generateTestTokenDto(
        email: String = "test@example.com",
        expirationMs: Long = 3600000L
    ): JwtTokenDto {
        val token = generateTestToken(email, expirationMs)
        val expiredAt = System.currentTimeMillis() + expirationMs
        return JwtTokenDto(token, expiredAt)
    }
    
    fun generateExpiredToken(email: String = "test@example.com"): String {
        val now = Date()
        val expiredAt = Date(now.time - 3600000L) // 1 hour ago
        
        return Jwts.builder()
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun generateInvalidToken(): String {
        return "invalid.jwt.token"
    }
    
    fun MockHttpServletRequestBuilder.withTestToken(
        email: String = "test@example.com"
    ): MockHttpServletRequestBuilder {
        val token = generateTestToken(email)
        return this.header("Authorization", "Bearer $token")
    }
    
    fun MockHttpServletRequestBuilder.withExpiredToken(
        email: String = "test@example.com"
    ): MockHttpServletRequestBuilder {
        val token = generateExpiredToken(email)
        return this.header("Authorization", "Bearer $token")
    }
    
    fun MockHttpServletRequestBuilder.withInvalidToken(): MockHttpServletRequestBuilder {
        return this.header("Authorization", "Bearer ${generateInvalidToken()}")
    }
}