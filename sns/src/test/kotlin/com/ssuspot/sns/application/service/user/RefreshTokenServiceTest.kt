package com.ssuspot.sns.application.service.user

import com.ssuspot.sns.application.dto.user.LoginDto
import com.ssuspot.sns.application.dto.user.RefreshTokenDto
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.domain.exceptions.user.InvalidRefreshTokenException
import com.ssuspot.sns.domain.model.user.RefreshToken
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.repository.user.RefreshTokenRepository
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@DisplayName("Refresh Token 서비스 테스트")
class RefreshTokenServiceTest {
    
    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository
    private lateinit var refreshTokenRepository: RefreshTokenRepository
    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var eventPublisher: ApplicationEventPublisher
    
    private val testEmail = "test@example.com"
    private val testPassword = "password123"
    private val testUser = User(
        id = 1L,
        userName = "testuser",
        email = testEmail,
        password = "encodedPassword",
        nickname = "testUser",
        profileMessage = "Hello",
        profileImageLink = null
    )
    
    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        refreshTokenRepository = mockk()
        jwtTokenProvider = mockk()
        passwordEncoder = mockk()
        eventPublisher = mockk()
        
        userService = UserService(
            userRepository = userRepository,
            userFollowRepository = mockk(),
            refreshTokenRepository = refreshTokenRepository,
            passwordEncoder = passwordEncoder,
            jwtTokenProvider = jwtTokenProvider,
            applicationEventPublisher = eventPublisher
        )
    }
    
    @Test
    @DisplayName("로그인 시 Refresh Token이 Redis에 저장된다")
    fun login_SavesRefreshTokenToRedis() {
        // given
        every { userRepository.findByEmail(testEmail) } returns testUser
        every { passwordEncoder.matches(testPassword, testUser.password) } returns true
        every { refreshTokenRepository.deleteAllByEmail(testEmail) } just runs
        every { refreshTokenRepository.save(any()) } returnsArgument 0
        every { jwtTokenProvider.generateAccessToken(testEmail) } returns mockk {
            every { token } returns "access-token"
            every { expiredIn } returns System.currentTimeMillis() + 3600000
        }
        every { jwtTokenProvider.generateRefreshToken(testEmail) } returns mockk {
            every { token } returns "refresh-token"
            every { expiredIn } returns System.currentTimeMillis() + 2592000000
        }
        
        // when
        val result = userService.login(LoginDto(testEmail, testPassword))
        
        // then
        assertThat(result.accessToken?.token).isEqualTo("access-token")
        assertThat(result.refreshToken?.token).isEqualTo("refresh-token")
        
        verify(exactly = 1) { refreshTokenRepository.deleteAllByEmail(testEmail) }
        verify(exactly = 1) { refreshTokenRepository.save(any()) }
    }
    
    @Test
    @DisplayName("유효한 Refresh Token으로 새로운 Access Token을 발급받는다")
    fun refresh_WithValidToken_ReturnsNewAccessToken() {
        // given
        val refreshTokenValue = "valid-refresh-token"
        val storedToken = RefreshToken(
            id = UUID.randomUUID().toString(),
            email = testEmail,
            ttl = 2592000,
            usageCount = 0
        )
        
        every { jwtTokenProvider.validateRefreshToken(refreshTokenValue) } returns true
        every { jwtTokenProvider.getUserEmailFromToken(refreshTokenValue) } returns testEmail
        every { userRepository.findByEmail(testEmail) } returns testUser
        every { refreshTokenRepository.findAllByEmail(testEmail) } returns listOf(storedToken)
        every { refreshTokenRepository.save(any()) } returnsArgument 0
        every { jwtTokenProvider.generateAccessToken(testEmail) } returns mockk {
            every { token } returns "new-access-token"
            every { expiredIn } returns System.currentTimeMillis() + 3600000
        }
        
        // when
        val result = userService.refresh(RefreshTokenDto(refreshTokenValue))
        
        // then
        assertThat(result.accessToken?.token).isEqualTo("new-access-token")
        assertThat(result.refreshToken?.token).isEqualTo(refreshTokenValue)
        
        verify(exactly = 1) { refreshTokenRepository.save(match { it.usageCount == 1 }) }
    }
    
    @Test
    @DisplayName("무효한 Refresh Token은 예외를 발생시킨다")
    fun refresh_WithInvalidToken_ThrowsException() {
        // given
        val invalidToken = "invalid-refresh-token"
        every { jwtTokenProvider.validateRefreshToken(invalidToken) } returns false
        
        // when & then
        assertThatThrownBy { userService.refresh(RefreshTokenDto(invalidToken)) }
            .isInstanceOf(InvalidRefreshTokenException::class.java)
    }
    
    @Test
    @DisplayName("Redis에 저장되지 않은 Refresh Token은 예외를 발생시킨다")
    fun refresh_WithUnregisteredToken_ThrowsException() {
        // given
        val unregisteredToken = "unregistered-token"
        every { jwtTokenProvider.validateRefreshToken(unregisteredToken) } returns true
        every { jwtTokenProvider.getUserEmailFromToken(unregisteredToken) } returns testEmail
        every { userRepository.findByEmail(testEmail) } returns testUser
        every { refreshTokenRepository.findAllByEmail(testEmail) } returns emptyList()
        
        // when & then
        assertThatThrownBy { userService.refresh(RefreshTokenDto(unregisteredToken)) }
            .isInstanceOf(InvalidRefreshTokenException::class.java)
    }
    
    @Test
    @DisplayName("만료된 Refresh Token은 예외를 발생시킨다")
    fun refresh_WithExpiredToken_ThrowsException() {
        // given
        val expiredTokenValue = "expired-token"
        val expiredToken = RefreshToken(
            id = UUID.randomUUID().toString(),
            email = testEmail,
            ttl = 1, // 1초
            createdAt = System.currentTimeMillis() - 3600000 // 1시간 전 생성
        )
        
        every { jwtTokenProvider.validateRefreshToken(expiredTokenValue) } returns true
        every { jwtTokenProvider.getUserEmailFromToken(expiredTokenValue) } returns testEmail
        every { userRepository.findByEmail(testEmail) } returns testUser
        every { refreshTokenRepository.findAllByEmail(testEmail) } returns listOf(expiredToken)
        
        // when & then
        assertThatThrownBy { userService.refresh(RefreshTokenDto(expiredTokenValue)) }
            .isInstanceOf(InvalidRefreshTokenException::class.java)
    }
    
    @Test
    @DisplayName("3회 이상 사용된 Refresh Token은 보안 위협으로 간주하여 모든 토큰을 삭제한다")
    fun refresh_WithOverusedToken_DeletesAllTokens() {
        // given
        val overusedTokenValue = "overused-token"
        val overusedToken = RefreshToken(
            id = UUID.randomUUID().toString(),
            email = testEmail,
            ttl = 2592000,
            usageCount = 3
        )
        
        every { jwtTokenProvider.validateRefreshToken(overusedTokenValue) } returns true
        every { jwtTokenProvider.getUserEmailFromToken(overusedTokenValue) } returns testEmail
        every { userRepository.findByEmail(testEmail) } returns testUser
        every { refreshTokenRepository.findAllByEmail(testEmail) } returns listOf(overusedToken)
        every { refreshTokenRepository.deleteAllByEmail(testEmail) } just runs
        
        // when & then
        assertThatThrownBy { userService.refresh(RefreshTokenDto(overusedTokenValue)) }
            .isInstanceOf(InvalidRefreshTokenException::class.java)
        
        verify(exactly = 1) { refreshTokenRepository.deleteAllByEmail(testEmail) }
    }
    
    @Test
    @DisplayName("Refresh Token Rotation - 2회 사용 후 새로운 Refresh Token 발급")
    fun refresh_TokenRotation_IssuesNewRefreshToken() {
        // given
        val oldRefreshToken = "old-refresh-token"
        val usedToken = RefreshToken(
            id = UUID.randomUUID().toString(),
            email = testEmail,
            ttl = 2592000,
            usageCount = 1 // 이미 1회 사용
        )
        
        every { jwtTokenProvider.validateRefreshToken(oldRefreshToken) } returns true
        every { jwtTokenProvider.getUserEmailFromToken(oldRefreshToken) } returns testEmail
        every { userRepository.findByEmail(testEmail) } returns testUser
        every { refreshTokenRepository.findAllByEmail(testEmail) } returns listOf(usedToken)
        every { refreshTokenRepository.save(any()) } returnsArgument 0
        every { refreshTokenRepository.delete(any()) } just runs
        every { jwtTokenProvider.generateAccessToken(testEmail) } returns mockk {
            every { token } returns "new-access-token"
            every { expiredIn } returns System.currentTimeMillis() + 3600000
        }
        every { jwtTokenProvider.generateRefreshToken(testEmail) } returns mockk {
            every { token } returns "new-refresh-token"
            every { expiredIn } returns System.currentTimeMillis() + 2592000000
        }
        
        // when
        val result = userService.refresh(RefreshTokenDto(oldRefreshToken))
        
        // then
        assertThat(result.accessToken?.token).isEqualTo("new-access-token")
        assertThat(result.refreshToken?.token).isEqualTo("new-refresh-token")
        
        verify(exactly = 1) { refreshTokenRepository.delete(any()) }
        verify(exactly = 2) { refreshTokenRepository.save(any()) } // 사용 카운트 증가 + 새 토큰 저장
    }
    
    @Test
    @DisplayName("로그아웃 시 모든 Refresh Token이 삭제된다")
    fun logout_DeletesAllRefreshTokens() {
        // given
        every { refreshTokenRepository.deleteAllByEmail(testEmail) } just runs
        
        // when
        userService.logout(testEmail)
        
        // then
        verify(exactly = 1) { refreshTokenRepository.deleteAllByEmail(testEmail) }
    }
    
    @Test
    @DisplayName("특정 Refresh Token만 폐기할 수 있다")
    fun revokeRefreshToken_RevokesSpecificToken() {
        // given
        val tokenId = UUID.randomUUID().toString()
        val token = RefreshToken(
            id = tokenId,
            email = testEmail,
            ttl = 2592000
        )
        
        every { refreshTokenRepository.findById(tokenId) } returns Optional.of(token)
        every { refreshTokenRepository.save(any()) } returnsArgument 0
        
        // when
        userService.revokeRefreshToken(testEmail, tokenId)
        
        // then
        verify(exactly = 1) { refreshTokenRepository.save(match { it.isRevoked }) }
    }
}