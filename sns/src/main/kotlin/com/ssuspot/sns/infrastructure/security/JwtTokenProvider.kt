package com.ssuspot.sns.infrastructure.security


import com.ssuspot.sns.application.dto.common.JwtTokenDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import io.jsonwebtoken.security.WeakKeyException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey
import jakarta.servlet.http.HttpServletRequest

@Component
open class JwtTokenProvider(
    @Value("\${app.jwt.secret}") private val jwtSecret: String,
    @Value("\${app.jwt.accessTokenExpirationMS}") private val accessTokenValidMilSecond: Long = 0,
    @Value("\${app.jwt.refreshTokenExpirationMS}") private val refreshTokenValidMilSecond: Long = 0
) {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    
    // Clock skew tolerance for expiration validation (5 minutes)
    private val clockSkewAllowance = Duration.ofMinutes(5)
    
    // Supported signature algorithms (only secure algorithms)
    private val supportedAlgorithms = setOf(
        SignatureAlgorithm.HS256,
        SignatureAlgorithm.HS384,
        SignatureAlgorithm.HS512
    )
    
    // Default algorithm for token generation
    private val defaultAlgorithm = SignatureAlgorithm.HS256

    fun generateAccessToken(email: String): JwtTokenDto {
        return generateToken(email, accessTokenValidMilSecond)
    }
    fun generateRefreshToken(email: String): JwtTokenDto {
        return generateToken(email, refreshTokenValidMilSecond)
    }
    
    /**
     * 토큰이 Access Token인지 Refresh Token인지 구분합니다.
     * @param token JWT 토큰
     * @return Access Token이면 true, Refresh Token이면 false
     */
    fun isAccessToken(token: String): Boolean {
        try {
            val claims = getClaimsFromToken(token) ?: return false
            val expiration = claims.expiration.time
            val issuedAt = claims.issuedAt.time
            val tokenDuration = expiration - issuedAt
            
            // Access Token의 만료 시간과 비교 (1.5배 이내면 Access Token으로 간주)
            return tokenDuration <= (accessTokenValidMilSecond * 1.5)
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * Refresh Token을 위한 특별한 검증을 수행합니다.
     * @param token Refresh Token
     * @return 토큰이 유효한지 여부
     */
    fun validateRefreshToken(token: String): Boolean {
        try {
            // 기본 검증 수행
            if (!validateTokenComprehensive(token)) {
                return false
            }
            
            // Refresh Token인지 확인
            return !isAccessToken(token)
        } catch (e: Exception) {
            return false
        }
    }

    fun generateToken(email: String, tokenValidMilSecond: Long): JwtTokenDto {
        val now = Date()
        val expiredIn = now.time + tokenValidMilSecond
        val token = Jwts.builder()
            .setClaims(mapOf("email" to email))
            .setIssuedAt(now)
            .setExpiration(Date(expiredIn))
            .signWith(secretKey, defaultAlgorithm)
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
        return createSecureParser()
            .parseClaimsJws(token)
            .body
    }
    
    /**
     * 보안이 강화된 JWT 파서를 생성합니다.
     * @return 설정된 JWT 파서
     */
    private fun createSecureParser(): JwtParser {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .setAllowedClockSkewSeconds(clockSkewAllowance.seconds)
            .requireIssuer(null) // Issuer 요구사항 비활성화 (현재 사용하지 않음)
            .build()
    }
    
    /**
     * 토큰의 만료 상태를 더 정확하게 검증합니다.
     * @param token JWT 토큰
     * @return 토큰이 유효한지 여부
     * @throws ExpiredJwtException 토큰이 만료된 경우
     * @throws SignatureException 서명이 유효하지 않은 경우
     */
    fun validateTokenExpiration(token: String): Boolean {
        try {
            val claims = getClaimsFromToken(token)
            if (claims == null) {
                return false
            }
            
            // 추가적인 만료 검증 - 현재 시간과 비교
            val now = Instant.now()
            val expiration = claims.expiration.toInstant()
            
            // Clock skew를 고려한 만료 검증
            return expiration.isAfter(now.minus(clockSkewAllowance))
        } catch (e: ExpiredJwtException) {
            throw e
        } catch (e: SignatureException) {
            throw e
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * 토큰이 곧 만료될지 확인합니다 (15분 이내)
     * @param token JWT 토큰
     * @return 토큰이 곧 만료될지 여부
     */
    fun isTokenExpiringSoon(token: String): Boolean {
        try {
            val claims = getClaimsFromToken(token) ?: return true
            val now = Instant.now()
            val expiration = claims.expiration.toInstant()
            val timeUntilExpiration = Duration.between(now, expiration)
            
            // 15분 이내에 만료되는 경우 true 반환
            return timeUntilExpiration.toMinutes() <= 15
        } catch (e: Exception) {
            return true
        }
    }
    
    /**
     * 토큰의 발급 시간이 유효한지 검증합니다.
     * @param token JWT 토큰
     * @return 발급 시간이 유효한지 여부
     */
    fun validateTokenIssuedAt(token: String): Boolean {
        try {
            val claims = getClaimsFromToken(token) ?: return false
            val issuedAt = claims.issuedAt?.toInstant() ?: return false
            val now = Instant.now()
            
            // 토큰이 미래에 발급되지 않았는지 확인 (clock skew 허용)
            return !issuedAt.isAfter(now.plus(clockSkewAllowance))
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * 토큰의 서명을 강화된 방식으로 검증합니다.
     * @param token JWT 토큰
     * @return 서명이 유효한지 여부
     * @throws SignatureException 서명이 유효하지 않은 경우
     * @throws UnsupportedJwtException 지원되지 않는 알고리즘인 경우
     * @throws MalformedJwtException 토큰 형식이 올바르지 않은 경우
     */
    fun validateTokenSignature(token: String): Boolean {
        try {
            // 토큰 헤더 먼저 검증 (서명 검증 전)
            if (!validateTokenHeader(token)) {
                return false
            }
            
            // 서명 검증을 위한 파싱 (Claims는 무시, 서명만 검증)
            createSecureParser().parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            throw e
        } catch (e: UnsupportedJwtException) {
            throw e
        } catch (e: MalformedJwtException) {
            throw e
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * 토큰 헤더의 유효성을 검증합니다.
     * @param token JWT 토큰
     * @return 헤더가 유효한지 여부
     */
    fun validateTokenHeader(token: String): Boolean {
        try {
            // 토큰을 직접 파싱하여 헤더만 확인
            val parts = token.split(".")
            if (parts.size != 3) {
                return false
            }
            
            // 헤더 부분 디코딩하여 알고리즘 확인
            val headerJson = String(java.util.Base64.getUrlDecoder().decode(parts[0]))
            
            // HS256 알고리즘 확인 (기본적으로 사용하는 알고리즘)
            val containsHS256 = headerJson.contains("\"alg\":\"HS256\"") || 
                               headerJson.contains("\"alg\": \"HS256\"")
            
            // none 알고리즘 사용 방지
            val containsNoneAlgorithm = headerJson.contains("\"alg\":\"none\"") || 
                                       headerJson.contains("\"alg\": \"none\"")
            
            return containsHS256 && !containsNoneAlgorithm
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * 서명 키의 무결성을 확인합니다.
     * @return 키가 안전한지 여부
     */
    fun validateSigningKeyIntegrity(): Boolean {
        try {
            // 키 길이 확인 (HS256은 최소 256비트/32바이트 필요)
            val keyBytes = secretKey.encoded
            if (keyBytes.size < 32) {
                return false
            }
            
            // 테스트 토큰 생성 및 검증으로 키 무결성 확인
            val testClaims = mapOf("test" to "validation")
            val testToken = Jwts.builder()
                .setClaims(testClaims)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + 60000))
                .signWith(secretKey, defaultAlgorithm)
                .compact()
            
            // 생성한 토큰을 다시 검증
            val parsedClaims = createSecureParser().parseClaimsJws(testToken).body
            return parsedClaims["test"] == "validation"
        } catch (e: WeakKeyException) {
            return false
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * 종합적인 토큰 검증을 수행합니다.
     * @param token JWT 토큰
     * @return 토큰이 모든 검증을 통과했는지 여부
     */
    fun validateTokenComprehensive(token: String): Boolean {
        try {
            return validateTokenHeader(token) &&
                   validateTokenSignature(token) &&
                   validateTokenExpiration(token) &&
                   validateTokenIssuedAt(token)
        } catch (e: Exception) {
            return false
        }
    }

    fun getAuthentication(claims: Claims): Authentication {
        return UsernamePasswordAuthenticationToken(claims["email"], null, null)
    }

    fun getUserEmailFromToken(token: String): String {
        return getClaimsFromToken(token)!!["email"] as String
    }
}