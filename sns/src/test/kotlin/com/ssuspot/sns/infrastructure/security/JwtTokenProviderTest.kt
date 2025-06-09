package com.ssuspot.sns.infrastructure.security

import com.ssuspot.sns.application.dto.common.JwtTokenDto
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockHttpServletRequest
import java.util.*

@DisplayName("JWT Token Provider 테스트")
class JwtTokenProviderTest {

    private lateinit var jwtTokenProvider: JwtTokenProvider
    private val testSecret = "test-secret-key-for-jwt-token-generation-must-be-at-least-32-characters-long"
    private val testEmail = "test@example.com"
    private val accessTokenExpiration = 3600000L // 1 hour
    private val refreshTokenExpiration = 86400000L // 24 hours

    @BeforeEach
    fun setUp() {
        jwtTokenProvider = JwtTokenProvider(
            jwtSecret = testSecret,
            accessTokenValidMilSecond = accessTokenExpiration,
            refreshTokenValidMilSecond = refreshTokenExpiration
        )
    }

    @Test
    @DisplayName("액세스 토큰을 성공적으로 생성한다")
    fun generateAccessToken_Success() {
        // when
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // then
        assertThat(token).isNotNull
        assertThat(token.token).isNotBlank()
        assertThat(token.expiredIn).isGreaterThan(System.currentTimeMillis())
        
        // 토큰 디코드 검증
        val claims = jwtTokenProvider.getClaimsFromToken(token.token)
        assertThat(claims).isNotNull
        assertThat(claims!!["email"]).isEqualTo(testEmail)
    }

    @Test
    @DisplayName("리프레시 토큰을 성공적으로 생성한다")
    fun generateRefreshToken_Success() {
        // when
        val token = jwtTokenProvider.generateRefreshToken(testEmail)

        // then
        assertThat(token).isNotNull
        assertThat(token.token).isNotBlank()
        assertThat(token.expiredIn).isGreaterThan(System.currentTimeMillis())
        
        // 리프레시 토큰이 액세스 토큰보다 만료 시간이 길다
        val accessToken = jwtTokenProvider.generateAccessToken(testEmail)
        assertThat(token.expiredIn).isGreaterThan(accessToken.expiredIn)
    }

    @Test
    @DisplayName("생성된 토큰에서 이메일을 추출한다")
    fun getUserEmailFromToken_Success() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val extractedEmail = jwtTokenProvider.getUserEmailFromToken(token.token)

        // then
        assertThat(extractedEmail).isEqualTo(testEmail)
    }

    @Test
    @DisplayName("HTTP 요청에서 Bearer 토큰을 추출한다")
    fun resolveToken_WithBearerToken_Success() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer ${token.token}")

        // when
        val claims = jwtTokenProvider.resolveToken(request)

        // then
        assertThat(claims).isNotNull
        assertThat(claims!!["email"]).isEqualTo(testEmail)
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 null을 반환한다")
    fun resolveToken_WithoutHeader_ReturnsNull() {
        // given
        val request = MockHttpServletRequest()

        // when
        val claims = jwtTokenProvider.resolveToken(request)

        // then
        assertThat(claims).isNull()
    }

    @Test
    @DisplayName("Bearer 프리픽스가 없는 토큰은 예외를 발생시킨다")
    fun resolveToken_WithoutBearerPrefix_ThrowsException() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)
        val request = MockHttpServletRequest()
        request.addHeader("Authorization", token.token)

        // when & then
        assertThatThrownBy { jwtTokenProvider.resolveToken(request) }
            .isInstanceOf(io.jsonwebtoken.io.DecodingException::class.java)
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 서명된 토큰은 검증에 실패한다")
    fun getClaimsFromToken_WithInvalidSecret_ThrowsException() {
        // given
        val wrongSecret = "wrong-secret-key-that-is-also-at-least-32-characters-long"
        val secretKey = Keys.hmacShaKeyFor(wrongSecret.toByteArray())
        val wrongToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000))
            .signWith(secretKey)
            .compact()

        // when & then
        assertThatThrownBy { jwtTokenProvider.getClaimsFromToken(wrongToken) }
            .isInstanceOf(SignatureException::class.java)
    }

    @Test
    @DisplayName("만료된 토큰은 검증에 실패한다")
    fun getClaimsFromToken_WithExpiredToken_ThrowsException() {
        // given
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val expiredToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date(System.currentTimeMillis() - 7200000))
            .setExpiration(Date(System.currentTimeMillis() - 3600000))
            .signWith(secretKey)
            .compact()

        // when & then
        assertThatThrownBy { jwtTokenProvider.getClaimsFromToken(expiredToken) }
            .isInstanceOf(ExpiredJwtException::class.java)
    }

    @Test
    @DisplayName("잘못된 형식의 토큰은 검증에 실패한다")
    fun getClaimsFromToken_WithMalformedToken_ThrowsException() {
        // given
        val malformedToken = "this.is.not.a.valid.jwt.token"

        // when & then
        assertThatThrownBy { jwtTokenProvider.getClaimsFromToken(malformedToken) }
            .isInstanceOf(MalformedJwtException::class.java)
    }

    @Test
    @DisplayName("Claims에서 Authentication 객체를 생성한다")
    fun getAuthentication_Success() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)
        val claims = jwtTokenProvider.getClaimsFromToken(token.token)

        // when
        val authentication = jwtTokenProvider.getAuthentication(claims!!)

        // then
        assertThat(authentication).isNotNull
        assertThat(authentication.principal).isEqualTo(testEmail)
        assertThat(authentication.authorities).isEmpty()
    }

    @Test
    @DisplayName("토큰 생성 시 현재 시간과 만료 시간이 올바르게 설정된다")
    fun generateToken_TimestampsAreCorrect() {
        // given
        val beforeCreation = System.currentTimeMillis()

        // when
        val token = jwtTokenProvider.generateToken(testEmail, accessTokenExpiration)
        val afterCreation = System.currentTimeMillis()

        // then
        val claims = jwtTokenProvider.getClaimsFromToken(token.token)
        val issuedAt = claims!!.issuedAt.time
        val expiration = claims.expiration.time

        // JWT는 초 단위로 저장되므로 1000ms 이내의 차이는 허용
        assertThat(issuedAt).isBetween(beforeCreation - 1000, afterCreation + 1000)
        assertThat(expiration - issuedAt).isBetween(accessTokenExpiration - 1000, accessTokenExpiration + 1000)
        assertThat(token.expiredIn).isBetween(expiration, expiration + 1000)
    }

    @Test
    @DisplayName("유효한 토큰의 만료 검증이 성공한다")
    fun validateTokenExpiration_ValidToken_ReturnsTrue() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val isValid = jwtTokenProvider.validateTokenExpiration(token.token)

        // then
        assertThat(isValid).isTrue()
    }

    @Test
    @DisplayName("만료된 토큰의 만료 검증이 실패한다")
    fun validateTokenExpiration_ExpiredToken_ThrowsException() {
        // given
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val expiredToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date(System.currentTimeMillis() - 7200000))
            .setExpiration(Date(System.currentTimeMillis() - 3600000))
            .signWith(secretKey)
            .compact()

        // when & then
        assertThatThrownBy { jwtTokenProvider.validateTokenExpiration(expiredToken) }
            .isInstanceOf(ExpiredJwtException::class.java)
    }

    @Test
    @DisplayName("잘못된 서명의 토큰 만료 검증이 실패한다")
    fun validateTokenExpiration_InvalidSignature_ThrowsException() {
        // given
        val wrongSecret = "wrong-secret-key-that-is-also-at-least-32-characters-long"
        val secretKey = Keys.hmacShaKeyFor(wrongSecret.toByteArray())
        val invalidToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000))
            .signWith(secretKey)
            .compact()

        // when & then
        assertThatThrownBy { jwtTokenProvider.validateTokenExpiration(invalidToken) }
            .isInstanceOf(io.jsonwebtoken.security.SignatureException::class.java)
    }

    @Test
    @DisplayName("잘못된 형식의 토큰 만료 검증이 실패한다")
    fun validateTokenExpiration_MalformedToken_ReturnsFalse() {
        // given
        val malformedToken = "malformed.token.format"

        // when
        val isValid = jwtTokenProvider.validateTokenExpiration(malformedToken)

        // then
        assertThat(isValid).isFalse()
    }

    @Test
    @DisplayName("곧 만료될 토큰을 올바르게 감지한다")
    fun isTokenExpiringSoon_ExpiringSoonToken_ReturnsTrue() {
        // given - 10분 후 만료되는 토큰 생성
        val shortExpirationTime = 10 * 60 * 1000L // 10 minutes
        val token = jwtTokenProvider.generateToken(testEmail, shortExpirationTime)

        // when
        val isExpiringSoon = jwtTokenProvider.isTokenExpiringSoon(token.token)

        // then
        assertThat(isExpiringSoon).isTrue()
    }

    @Test
    @DisplayName("충분한 시간이 남은 토큰은 곧 만료되지 않는다")
    fun isTokenExpiringSoon_NotExpiringSoonToken_ReturnsFalse() {
        // given - 1시간 후 만료되는 토큰 생성
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val isExpiringSoon = jwtTokenProvider.isTokenExpiringSoon(token.token)

        // then
        assertThat(isExpiringSoon).isFalse()
    }

    @Test
    @DisplayName("이미 만료된 토큰은 곧 만료된다고 판단한다")
    fun isTokenExpiringSoon_ExpiredToken_ReturnsTrue() {
        // given
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val expiredToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date(System.currentTimeMillis() - 7200000))
            .setExpiration(Date(System.currentTimeMillis() - 3600000))
            .signWith(secretKey)
            .compact()

        // when
        val isExpiringSoon = jwtTokenProvider.isTokenExpiringSoon(expiredToken)

        // then
        assertThat(isExpiringSoon).isTrue()
    }

    @Test
    @DisplayName("유효한 발급 시간의 토큰 검증이 성공한다")
    fun validateTokenIssuedAt_ValidIssuedAt_ReturnsTrue() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val isValid = jwtTokenProvider.validateTokenIssuedAt(token.token)

        // then
        assertThat(isValid).isTrue()
    }

    @Test
    @DisplayName("미래에 발급된 토큰 검증이 실패한다")
    fun validateTokenIssuedAt_FutureIssuedAt_ReturnsFalse() {
        // given - 미래 시간에 발급된 토큰 생성
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val futureTime = System.currentTimeMillis() + 3600000 // 1시간 후
        val futureToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date(futureTime))
            .setExpiration(Date(futureTime + 3600000))
            .signWith(secretKey)
            .compact()

        // when
        val isValid = jwtTokenProvider.validateTokenIssuedAt(futureToken)

        // then
        assertThat(isValid).isFalse()
    }

    @Test
    @DisplayName("발급 시간이 없는 토큰 검증이 실패한다")
    fun validateTokenIssuedAt_NoIssuedAt_ReturnsFalse() {
        // given - 발급 시간이 없는 토큰 생성
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val tokenWithoutIssuedAt = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setExpiration(Date(System.currentTimeMillis() + 3600000))
            .signWith(secretKey)
            .compact()

        // when
        val isValid = jwtTokenProvider.validateTokenIssuedAt(tokenWithoutIssuedAt)

        // then
        assertThat(isValid).isFalse()
    }

    @Test
    @DisplayName("잘못된 토큰의 발급 시간 검증이 실패한다")
    fun validateTokenIssuedAt_InvalidToken_ReturnsFalse() {
        // given
        val malformedToken = "invalid.token.format"

        // when
        val isValid = jwtTokenProvider.validateTokenIssuedAt(malformedToken)

        // then
        assertThat(isValid).isFalse()
    }

    @Test
    @DisplayName("유효한 토큰의 서명 검증이 성공한다")
    fun validateTokenSignature_ValidToken_ReturnsTrue() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val isValidSignature = jwtTokenProvider.validateTokenSignature(token.token)

        // then
        assertThat(isValidSignature).isTrue()
    }

    @Test
    @DisplayName("잘못된 서명의 토큰 검증이 실패한다")
    fun validateTokenSignature_InvalidSignature_ThrowsException() {
        // given
        val wrongSecret = "wrong-secret-key-that-is-also-at-least-32-characters-long"
        val secretKey = Keys.hmacShaKeyFor(wrongSecret.toByteArray())
        val invalidToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()

        // when & then
        assertThatThrownBy { jwtTokenProvider.validateTokenSignature(invalidToken) }
            .isInstanceOf(io.jsonwebtoken.security.SignatureException::class.java)
    }

    @Test
    @DisplayName("잘못된 형식의 토큰 서명 검증이 실패한다")
    fun validateTokenSignature_MalformedToken_ReturnsFalse() {
        // given
        val malformedToken = "malformed.token.format"

        // when
        val isValidSignature = jwtTokenProvider.validateTokenSignature(malformedToken)

        // then
        assertThat(isValidSignature).isFalse()
    }

    @Test
    @DisplayName("유효한 토큰 헤더 검증이 성공한다")
    fun validateTokenHeader_ValidHeader_ReturnsTrue() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val isValidHeader = jwtTokenProvider.validateTokenHeader(token.token)

        // then
        assertThat(isValidHeader).isTrue()
    }

    @Test
    @DisplayName("none 알고리즘을 사용한 토큰 헤더 검증이 실패한다")
    fun validateTokenHeader_NoneAlgorithm_ReturnsFalse() {
        // given - 'none' 알고리즘을 사용한 위험한 토큰 생성
        val noneAlgToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000))
            .compact() // 서명 없이 생성

        // when
        val isValidHeader = jwtTokenProvider.validateTokenHeader(noneAlgToken)

        // then
        assertThat(isValidHeader).isFalse()
    }

    @Test
    @DisplayName("잘못된 형식의 토큰 헤더 검증이 실패한다")
    fun validateTokenHeader_InvalidFormat_ReturnsFalse() {
        // given
        val invalidFormatToken = "invalid.format"

        // when
        val isValidHeader = jwtTokenProvider.validateTokenHeader(invalidFormatToken)

        // then
        assertThat(isValidHeader).isFalse()
    }

    @Test
    @DisplayName("서명 키 무결성 검증이 성공한다")
    fun validateSigningKeyIntegrity_ValidKey_ReturnsTrue() {
        // when
        val isKeyIntegrityValid = jwtTokenProvider.validateSigningKeyIntegrity()

        // then
        assertThat(isKeyIntegrityValid).isTrue()
    }

    @Test
    @DisplayName("약한 키를 사용한 JWT 제공자는 키 무결성 검증이 실패한다")
    fun validateSigningKeyIntegrity_WeakKey_ReturnsFalse() {
        // given - 약한 키로 새로운 JWT 제공자 생성 (하지만 예외 발생 가능)
        val weakSecret = "weak"
        
        // when & then - 약한 키는 생성 단계에서 예외가 발생할 수 있음
        assertThatThrownBy {
            JwtTokenProvider(
                jwtSecret = weakSecret,
                accessTokenValidMilSecond = accessTokenExpiration,
                refreshTokenValidMilSecond = refreshTokenExpiration
            )
        }.isInstanceOf(io.jsonwebtoken.security.WeakKeyException::class.java)
    }

    @Test
    @DisplayName("종합적인 토큰 검증이 성공한다")
    fun validateTokenComprehensive_ValidToken_ReturnsTrue() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)

        // when
        val isComprehensivelyValid = jwtTokenProvider.validateTokenComprehensive(token.token)

        // then
        assertThat(isComprehensivelyValid).isTrue()
    }

    @Test
    @DisplayName("만료된 토큰의 종합적인 검증이 실패한다")
    fun validateTokenComprehensive_ExpiredToken_ReturnsFalse() {
        // given
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val expiredToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date(System.currentTimeMillis() - 7200000))
            .setExpiration(Date(System.currentTimeMillis() - 3600000))
            .signWith(secretKey)
            .compact()

        // when
        val isComprehensivelyValid = jwtTokenProvider.validateTokenComprehensive(expiredToken)

        // then
        assertThat(isComprehensivelyValid).isFalse()
    }

    @Test
    @DisplayName("잘못된 서명을 가진 토큰의 종합적인 검증이 실패한다")
    fun validateTokenComprehensive_InvalidSignature_ReturnsFalse() {
        // given
        val wrongSecret = "wrong-secret-key-that-is-also-at-least-32-characters-long"
        val secretKey = Keys.hmacShaKeyFor(wrongSecret.toByteArray())
        val invalidToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000))
            .signWith(secretKey)
            .compact()

        // when
        val isComprehensivelyValid = jwtTokenProvider.validateTokenComprehensive(invalidToken)

        // then
        assertThat(isComprehensivelyValid).isFalse()
    }

    @Test
    @DisplayName("지원되지 않는 알고리즘의 토큰 헤더 검증이 실패한다")
    fun validateTokenHeader_UnsupportedAlgorithm_ReturnsFalse() {
        // given - RS256 알고리즘을 시뮬레이션하는 토큰 헤더 (실제로는 생성 불가능하므로 헤더만 검증)
        val base64Header = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".toByteArray())
        val dummyPayload = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"email\":\"test@example.com\"}".toByteArray())
        val unsupportedAlgToken = "$base64Header.$dummyPayload.dummy-signature"

        // when
        val isValidHeader = jwtTokenProvider.validateTokenHeader(unsupportedAlgToken)

        // then
        assertThat(isValidHeader).isFalse()
    }
    
    @Test
    @DisplayName("Access Token 식별이 올바르게 동작한다")
    fun isAccessToken_IdentifiesCorrectly() {
        // given
        val accessToken = jwtTokenProvider.generateAccessToken(testEmail)
        val refreshToken = jwtTokenProvider.generateRefreshToken(testEmail)
        
        // when
        val isAccessTokenResult = jwtTokenProvider.isAccessToken(accessToken.token)
        val isRefreshTokenResult = jwtTokenProvider.isAccessToken(refreshToken.token)
        
        // then
        assertThat(isAccessTokenResult).isTrue()
        assertThat(isRefreshTokenResult).isFalse()
    }
    
    @Test
    @DisplayName("Refresh Token 검증이 올바르게 동작한다")
    fun validateRefreshToken_ValidatesCorrectly() {
        // given
        val refreshToken = jwtTokenProvider.generateRefreshToken(testEmail)
        val accessToken = jwtTokenProvider.generateAccessToken(testEmail)
        
        // when
        val isValidRefreshToken = jwtTokenProvider.validateRefreshToken(refreshToken.token)
        val isValidAccessTokenAsRefresh = jwtTokenProvider.validateRefreshToken(accessToken.token)
        
        // then
        assertThat(isValidRefreshToken).isTrue()
        assertThat(isValidAccessTokenAsRefresh).isFalse()
    }
    
    @Test
    @DisplayName("만료된 Refresh Token 검증이 실패한다")
    fun validateRefreshToken_ExpiredToken_ReturnsFalse() {
        // given
        val secretKey = Keys.hmacShaKeyFor(testSecret.toByteArray())
        val expiredRefreshToken = Jwts.builder()
            .setClaims(mapOf("email" to testEmail))
            .setIssuedAt(Date(System.currentTimeMillis() - 2592000000 - 3600000)) // 30일 이상 전
            .setExpiration(Date(System.currentTimeMillis() - 3600000)) // 1시간 전 만료
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
        
        // when
        val isValid = jwtTokenProvider.validateRefreshToken(expiredRefreshToken)
        
        // then
        assertThat(isValid).isFalse()
    }
    
    @Test
    @DisplayName("잘못된 형식의 토큰은 Access Token 식별에 실패한다")
    fun isAccessToken_MalformedToken_ReturnsFalse() {
        // given
        val malformedToken = "malformed.token.format"
        
        // when
        val result = jwtTokenProvider.isAccessToken(malformedToken)
        
        // then
        assertThat(result).isFalse()
    }
}