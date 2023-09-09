package com.ssuspot.sns.core.user.service

import com.ssuspot.sns.core.user.model.dto.AuthTokenDto
import com.ssuspot.sns.core.user.model.dto.LoginDto
import com.ssuspot.sns.core.user.model.dto.RegisterDto
import com.ssuspot.sns.core.user.model.entity.User
import com.ssuspot.sns.core.user.model.repository.UserRepository
import com.ssuspot.sns.security.JwtTokenProvider
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
        val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtTokenProvider: JwtTokenProvider,
) {
    @Transactional(rollbackFor = [Exception::class])
    fun createUser(
            registerDto: RegisterDto
    ){
        //TODO: password encoder 적용, refreshtoken 적용
        userRepository.save(
                User(
                        userName = registerDto.userName,
                        email = registerDto.email,
                        password = registerDto.password,
                        nickname = registerDto.nickname,
                        profileMessage = registerDto.profileMessage,
                        profileImageLink = registerDto.profileImageLink
                )
        )
    }
    @Transactional
    fun login(
            loginDto: LoginDto
    ): AuthTokenDto{
        val user = userRepository.findByEmail(loginDto.email) ?: throw Exception("User Not Found")
        if(user.password != loginDto.password) throw Exception("Password Not Match")

        //TODO: refresh,access token 생성
        val accessToken = jwtTokenProvider.generateAccessToken(user.email)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.email)

        //TODO: refresh token spring cache에 저장
        saveRefreshToken(user.email, refreshToken.token)


        return AuthTokenDto(accessToken, refreshToken)
    }

    //TODO: logout

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
    }
}