package com.ssuspot.sns.application.service.user

import com.ssuspot.sns.domain.model.user.event.RegisteredUserEvent
import com.ssuspot.sns.application.dto.user.AuthTokenDto
import com.ssuspot.sns.application.dto.user.LoginDto
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
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
    fun registerProcess(
            registerDto: RegisterDto
    ) {
        val savedUser = createUser(registerDto)
        val registeredUserEvent = RegisteredUserEvent(savedUser.id, savedUser.email)
        try{
            applicationEventPublisher.publishEvent(registeredUserEvent)
        } catch (e: Exception) {
            println("event publish error")
        }
    }

    fun findValidUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
                ?: throw Exception("user not found")
    }

    @Transactional
    fun login(
            loginDto: LoginDto
    ): AuthTokenDto {
        val user = userRepository.findByEmail(loginDto.email) ?: throw Exception("email or password is wrong")

        if (!passwordEncoder.matches(loginDto.password,user.password)) throw Exception("email or password is wrong")

        //refresh,access token 생성
        val accessToken = jwtTokenProvider.generateAccessToken(user.email)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.email)

        //refresh token spring cache에 저장
        saveRefreshToken(user.email, refreshToken.token)


        return AuthTokenDto(accessToken, refreshToken)
    }
    // logout 기능 구현 필요

    @CachePut(value = ["refreshToken"], key = "#email")
    fun saveRefreshToken(email: String, refreshToken: String): String {
        return refreshToken
    }

    @Cacheable(value = ["refreshToken"], key = "#email")
    fun getRefreshToken(email: String): String? {
        return null  // 캐시 조회 성공시 null 반환 x
    }

    @CacheEvict(value = ["refreshToken"], key = "#email")
    fun deleteRefreshToken(email: String) {
        // 캐시 삭제
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


}