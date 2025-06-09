package com.ssuspot.sns.application.service.user

import com.ssuspot.sns.application.dto.user.LoginDto
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.application.dto.user.UpdateUserDataDto
import com.ssuspot.sns.domain.exceptions.user.EmailExistException
import com.ssuspot.sns.domain.exceptions.user.UserNotFoundException
import com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.repository.post.UserFollowRepository
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import com.ssuspot.sns.support.ServiceTest
import com.ssuspot.sns.support.fixture.UserFixture
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ServiceTest
@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userFollowRepository: UserFollowRepository

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @MockK
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    @DisplayName("회원가입 - 성공")
    fun `should register user successfully`() {
        // given
        val registerDto = RegisterDto(
            email = "test@example.com",
            password = "password123",
            userName = "testuser",
            nickname = "Test User",
            profileMessage = "Hello",
            profileImageLink = "https://example.com/profile.jpg"
        )

        val encodedPassword = "encoded-password"
        val savedUser = UserFixture.createUser(
            id = 1L,
            email = registerDto.email,
            password = encodedPassword,
            userName = registerDto.userName,
            nickname = registerDto.nickname
        )

        every { userRepository.findByEmail(registerDto.email) } returns null
        every { passwordEncoder.encode(registerDto.password) } returns encodedPassword
        every { userRepository.save(any()) } returns savedUser
        every { applicationEventPublisher.publishEvent(any()) } just Runs

        // when
        val result = userService.registerProcess(registerDto)

        // then
        assertThat(result.email).isEqualTo(registerDto.email)
        assertThat(result.userName).isEqualTo(registerDto.userName)
        assertThat(result.nickname).isEqualTo(registerDto.nickname)
        
        verify { userRepository.findByEmail(registerDto.email) }
        verify { passwordEncoder.encode(registerDto.password) }
        verify { userRepository.save(any()) }
        verify { applicationEventPublisher.publishEvent(any()) }
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일로 실패")
    fun `should fail to register with duplicate email`() {
        // given
        val registerDto = RegisterDto(
            email = "duplicate@example.com",
            password = "password123",
            userName = "testuser",
            nickname = "Test User",
            profileMessage = null,
            profileImageLink = null
        )

        val existingUser = UserFixture.createUser(email = registerDto.email)

        every { userRepository.findByEmail(registerDto.email) } returns existingUser

        // when & then
        assertThatThrownBy { userService.registerProcess(registerDto) }
            .isInstanceOf(EmailExistException::class.java)

        verify { userRepository.findByEmail(registerDto.email) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    @DisplayName("로그인 - 성공")
    fun `should login successfully with valid credentials`() {
        // given
        val loginDto = LoginDto(
            email = "test@example.com",
            password = "password123"
        )

        val encodedPassword = "encoded-password"
        val user = UserFixture.createUser(
            email = loginDto.email,
            password = encodedPassword
        )

        val accessToken = mockk<com.ssuspot.sns.application.dto.common.JwtTokenDto>()
        val refreshToken = mockk<com.ssuspot.sns.application.dto.common.JwtTokenDto>()

        every { userRepository.findByEmail(loginDto.email) } returns user
        every { passwordEncoder.matches(loginDto.password, encodedPassword) } returns true
        every { jwtTokenProvider.generateAccessToken(loginDto.email) } returns accessToken
        every { jwtTokenProvider.generateRefreshToken(loginDto.email) } returns refreshToken

        // when
        val result = userService.login(loginDto)

        // then
        assertThat(result.accessToken).isEqualTo(accessToken)
        assertThat(result.refreshToken).isEqualTo(refreshToken)

        verify { userRepository.findByEmail(loginDto.email) }
        verify { passwordEncoder.matches(loginDto.password, encodedPassword) }
        verify { jwtTokenProvider.generateAccessToken(loginDto.email) }
        verify { jwtTokenProvider.generateRefreshToken(loginDto.email) }
    }

    @Test
    @DisplayName("로그인 - 존재하지 않는 사용자로 실패")
    fun `should fail to login with non-existent user`() {
        // given
        val loginDto = LoginDto(
            email = "nonexistent@example.com",
            password = "password123"
        )

        every { userRepository.findByEmail(loginDto.email) } returns null

        // when & then
        assertThatThrownBy { userService.login(loginDto) }
            .isInstanceOf(UserNotFoundException::class.java)

        verify { userRepository.findByEmail(loginDto.email) }
        verify(exactly = 0) { passwordEncoder.matches(any(), any()) }
    }

    @Test
    @DisplayName("로그인 - 잘못된 비밀번호로 실패")
    fun `should fail to login with wrong password`() {
        // given
        val loginDto = LoginDto(
            email = "test@example.com",
            password = "wrongpassword"
        )

        val user = UserFixture.createUser(
            email = loginDto.email,
            password = "encoded-password"
        )

        every { userRepository.findByEmail(loginDto.email) } returns user
        every { passwordEncoder.matches(loginDto.password, user.password) } returns false

        // when & then
        assertThatThrownBy { userService.login(loginDto) }
            .isInstanceOf(UserPasswordIncorrectException::class.java)

        verify { userRepository.findByEmail(loginDto.email) }
        verify { passwordEncoder.matches(loginDto.password, user.password) }
        verify(exactly = 0) { jwtTokenProvider.generateAccessToken(any()) }
    }

    @Test
    @DisplayName("프로필 업데이트 - 성공")
    fun `should update user profile successfully`() {
        // given
        val email = "test@example.com"
        val updateDto = UpdateUserDataDto(
            email = email,
            userName = "updateduser",
            nickname = "Updated User",
            profileMessage = "Updated message",
            profileImageLink = "https://example.com/new-profile.jpg"
        )

        val user = UserFixture.createUser(email = email)

        every { userRepository.findByEmail(email) } returns user

        // when
        val result = userService.updateProfile(updateDto)

        // then
        assertThat(result.nickname).isEqualTo(updateDto.nickname)
        assertThat(user.nickname).isEqualTo(updateDto.nickname)
        assertThat(user.profileMessage).isEqualTo(updateDto.profileMessage)
        assertThat(user.profileImageLink).isEqualTo(updateDto.profileImageLink)

        verify { userRepository.findByEmail(email) }
    }

    @Test
    @DisplayName("사용자 정보 조회 - 성공")
    fun `should get user info successfully`() {
        // given
        val email = "test@example.com"
        val user = UserFixture.createUser(email = email)

        every { userRepository.findByEmail(email) } returns user

        // when
        val result = userService.getUserInfo(email)

        // then
        assertThat(result.email).isEqualTo(email)
        assertThat(result).isEqualTo(user)

        verify { userRepository.findByEmail(email) }
    }

    @Test
    @DisplayName("사용자 정보 조회 - 존재하지 않는 사용자로 실패")
    fun `should fail to get info of non-existent user`() {
        // given
        val email = "nonexistent@example.com"

        every { userRepository.findByEmail(email) } returns null

        // when & then
        assertThatThrownBy { userService.getUserInfo(email) }
            .isInstanceOf(UserNotFoundException::class.java)

        verify { userRepository.findByEmail(email) }
    }

    @Test
    @DisplayName("이메일로 사용자 찾기 - 성공")
    fun `should find user by email successfully`() {
        // given
        val email = "test@example.com"
        val user = UserFixture.createUser(email = email)

        every { userRepository.findByEmail(email) } returns user

        // when
        val result = userService.findValidUserByEmail(email)

        // then
        assertThat(result).isEqualTo(user)

        verify { userRepository.findByEmail(email) }
    }

    @Test
    @DisplayName("ID로 사용자 찾기 - 성공")
    fun `should find user by id successfully`() {
        // given
        val userId = 1L
        val user = UserFixture.createUser(id = userId)

        every { userRepository.findById(userId) } returns Optional.of(user)

        // when
        val result = userService.getValidUser(userId)

        // then
        assertThat(result).isEqualTo(user)

        verify { userRepository.findById(userId) }
    }

    @Test
    @DisplayName("ID로 사용자 찾기 - 존재하지 않는 ID로 실패")
    fun `should fail to find user with non-existent id`() {
        // given
        val userId = 999L

        every { userRepository.findById(userId) } returns Optional.empty()

        // when & then
        assertThatThrownBy { userService.getValidUser(userId) }
            .isInstanceOf(UserNotFoundException::class.java)

        verify { userRepository.findById(userId) }
    }
}