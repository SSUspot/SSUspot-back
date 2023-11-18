package com.ssuspot.sns.application.service.user

import com.ssuspot.sns.application.dto.common.JwtTokenDto
import com.ssuspot.sns.domain.model.user.event.RegisteredUserEvent
import com.ssuspot.sns.application.dto.user.AuthTokenDto
import com.ssuspot.sns.application.dto.user.LoginDto
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.application.dto.user.RegisterResponseDto
import com.ssuspot.sns.domain.exceptions.user.EmailExistException
import com.ssuspot.sns.domain.exceptions.user.UserNotFoundException
import com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.aop.CacheUser
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun getUserInfo(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw UserNotFoundException()
    }
    fun registerProcess(
        registerDto: RegisterDto
    ): RegisterResponseDto {
        val user = userRepository.findByEmail(registerDto.email)
        if(user != null) throw EmailExistException()

        val savedUser = createUser(registerDto)
        val registeredUserEvent = RegisteredUserEvent(savedUser.id, savedUser.email)
        try {
            applicationEventPublisher.publishEvent(registeredUserEvent)
        } catch (e: Exception) {
            println("event publish error")
        }

        return RegisterResponseDto(
            savedUser.id!!,
            savedUser.email,
            savedUser.userName,
            savedUser.nickname,
            savedUser.profileMessage,
            savedUser.profileImageLink
        )
    }

    fun findValidUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw UserNotFoundException()
    }

    @Transactional
    fun login(
        loginDto: LoginDto
    ): AuthTokenDto {
        val user = userRepository.findByEmail(loginDto.email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(loginDto.password, user.password)) throw UserPasswordIncorrectException()

        //refresh,access token 생성
        val accessToken = generateAccessToken(user.email)
        val refreshToken = generateRefreshToken(user.email)
        return AuthTokenDto(accessToken, refreshToken)
    }
    fun getValidUserByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw UserNotFoundException()
    }
    fun getValidUser(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException() }
    }
    private fun createUser(
        registerDto: RegisterDto
    ): User {
        return userRepository.save(
            User(
                userName = registerDto.userName,
                email = registerDto.email,
                password = passwordEncoder.encode(registerDto.password),
                nickname = registerDto.nickname,
                profileMessage = registerDto.profileMessage,
                profileImageLink = registerDto.profileImageLink
            )
        )
    }

    private fun generateAccessToken(email: String): JwtTokenDto {
        return jwtTokenProvider.generateAccessToken(email)
    }

    private fun generateRefreshToken(email: String): JwtTokenDto = CacheUser.cache("JWT","Email:${email}") {
        return@cache jwtTokenProvider.generateRefreshToken(email)
    }
}