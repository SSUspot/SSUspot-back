package com.ssuspot.sns.application.service.user

import com.ssuspot.sns.application.dto.common.JwtTokenDto
import com.ssuspot.sns.application.dto.user.*
import com.ssuspot.sns.domain.exceptions.user.AlreadyFollowedUserException
import com.ssuspot.sns.domain.model.user.event.RegisteredUserEvent
import com.ssuspot.sns.domain.exceptions.user.EmailExistException
import com.ssuspot.sns.domain.exceptions.user.UserNotFoundException
import com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException
import com.ssuspot.sns.domain.exceptions.user.InvalidRefreshTokenException
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.user.entity.UserFollow
import com.ssuspot.sns.domain.model.user.RefreshToken
import com.ssuspot.sns.infrastructure.aop.CacheUser
import com.ssuspot.sns.infrastructure.repository.post.UserFollowRepository
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.infrastructure.repository.user.RefreshTokenRepository
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.ssuspot.sns.infrastructure.monitoring.BusinessMetricsCollector
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userFollowRepository: UserFollowRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val businessMetricsCollector: BusinessMetricsCollector
) {
    @Transactional(readOnly = true)
    @Cacheable(value = ["userInfo"], key = "#userId + '_' + #email")
    fun getSpecificUser(
        userId: Long,
        email: String
    ): UserInfoResponseDto {
        val targetUser = getValidUser(userId)
        val requestedUser = getValidUserByEmail(email)
        val followed = userFollowRepository.findByFollowingUserAndFollowedUser(requestedUser, targetUser) != null
        return UserInfoResponseDto(
            id = targetUser.id!!,
            email = targetUser.email,
            userName = targetUser.userName,
            nickname = targetUser.nickname,
            profileMessage = targetUser.profileMessage,
            profileImageLink = targetUser.profileImageLink,
            followed = followed
        )
    }

    @Transactional
    fun registerProcess(
        registerDto: RegisterDto
    ): UserResponseDto {
        val user = userRepository.findByEmail(registerDto.email)
        if(user != null) throw EmailExistException()

        val savedUser = createUser(registerDto)
        
        // 비즈니스 메트릭 수집
        businessMetricsCollector.recordUserRegistration(
            userId = savedUser.id.toString(),
            email = savedUser.email
        )
        
        val registeredUserEvent = RegisteredUserEvent(savedUser.id, savedUser.email)
        try {
            applicationEventPublisher.publishEvent(registeredUserEvent)
        } catch (e: Exception) {
            println("event publish error")
        }

        return savedUser.toDto()
    }

    // 사용자 프로필 수정 api
    @Transactional
    @CacheEvict(value = ["userInfo", "users"], allEntries = true)
    fun updateProfile(
        updateUserDataDto: UpdateUserDataDto
    ): UserResponseDto {
        val user = getValidUserByEmail(updateUserDataDto.email)
        user.updateNickname(updateUserDataDto.nickname)
        user.updateProfileMessage(updateUserDataDto.profileMessage)
        user.updateProfileImageLink(updateUserDataDto.profileImageLink)
        return user.toDto()
    }

    //실행시 evict하여 캐시 삭제
    @Transactional
    fun login(
        loginDto: LoginDto
    ): AuthTokenDto {
        val startTime = System.currentTimeMillis()
        
        val user = userRepository.findByEmail(loginDto.email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(loginDto.password, user.password)) throw UserPasswordIncorrectException()

        // 기존 Refresh 토큰 모두 삭제 (중복 로그인 방지)
        refreshTokenRepository.deleteAllByEmail(user.email)

        // 새로운 토큰 생성
        val accessToken = generateAccessToken(user.email)
        val refreshToken = generateRefreshToken(user.email)
        
        // 비즈니스 메트릭 수집
        val duration = System.currentTimeMillis() - startTime
        businessMetricsCollector.recordUserLogin(
            userId = user.id.toString(),
            duration = duration
        )
        
        // Refresh 토큰을 Redis에 저장
        val refreshTokenEntity = RefreshToken(
            id = UUID.randomUUID().toString(),
            email = user.email,
            ttl = 2592000 // 30일 (초 단위)
        )
        refreshTokenRepository.save(refreshTokenEntity)
        
        return AuthTokenDto(accessToken, refreshToken)
    }

    @Transactional
    @CacheEvict(value = ["follows", "userInfo"], allEntries = true)
    fun follow(
        followingRequestDto: FollowingRequestDto
    ): FollowUserResponseDto{
        val user = getValidUserByEmail(followingRequestDto.email)
        val followedUser = getValidUser(followingRequestDto.userId)
        if(userFollowRepository.findByFollowingUserAndFollowedUser(user, followedUser)!=null) throw AlreadyFollowedUserException()
        val userFollow = userFollowRepository.save(
            UserFollow(
                followingUser = user,
                followedUser = followedUser
            )
        )
        
        // 비즈니스 메트릭 수집
        businessMetricsCollector.recordFollowAction(
            followingUserId = user.id.toString(),
            followedUserId = followedUser.id.toString(),
            isFollow = true
        )
        
        return FollowUserResponseDto(
            id = userFollow.id!!,
            userId = userFollow.followedUser.id!!,
            userName = userFollow.followedUser.userName,
            nickname = userFollow.followedUser.nickname,
            profileImageLink = userFollow.followedUser.profileImageLink
        )
    }

    @Transactional
    @CacheEvict(value = ["follows", "userInfo"], allEntries = true)
    fun unfollow(
        followingRequestDto: FollowingRequestDto
    ){
        val user = getValidUserByEmail(followingRequestDto.email)
        val followedUser = getValidUser(followingRequestDto.userId)
        val userFollow = userFollowRepository.findByFollowingUserAndFollowedUser(user, followedUser) ?: throw UserNotFoundException()
        userFollowRepository.delete(userFollow)
        
        // 비즈니스 메트릭 수집
        businessMetricsCollector.recordFollowAction(
            followingUserId = user.id.toString(),
            followedUserId = followedUser.id.toString(),
            isFollow = false
        )
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["follows"], key = "'following_' + #email")
    fun getMyFollowingList(email: String): List<FollowUserResponseDto>{
        val user = getValidUserByEmail(email)
        return user.following.map {
            FollowUserResponseDto(
                id = it.id!!,
                userId = it.followedUser.id!!,
                userName = it.followedUser.userName,
                nickname = it.followedUser.nickname,
                profileImageLink = it.followedUser.profileImageLink
            )
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["follows"], key = "'following_user_' + #userId")
    fun getFollowingListOfUser(userId:Long): List<FollowUserResponseDto>{
        val user = getValidUser(userId)
        return user.following.map {
            FollowUserResponseDto(
                id = it.id!!,
                userId = it.followedUser.id!!,
                userName = it.followedUser.userName,
                nickname = it.followedUser.nickname,
                profileImageLink = it.followedUser.profileImageLink
            )
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["follows"], key = "'followers_' + #email")
    fun getMyFollowerList(email: String): List<FollowUserResponseDto>{
        val user = getValidUserByEmail(email)
        return user.followers.map {
            FollowUserResponseDto(
                id = it.id!!,
                userId = it.followingUser.id!!,
                userName = it.followingUser.userName,
                nickname = it.followingUser.nickname,
                profileImageLink = it.followingUser.profileImageLink
            )
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["follows"], key = "'followers_user_' + #userId")
    fun getFollowerListOfUser(userId:Long): List<FollowUserResponseDto>{
        val user = getValidUser(userId)
        return user.followers.map {
            FollowUserResponseDto(
                id = it.id!!,
                userId = it.followingUser.id!!,
                userName = it.followingUser.userName,
                nickname = it.followingUser.nickname,
                profileImageLink = it.followingUser.profileImageLink
            )
        }
    }

    @Transactional
    fun refresh(dto: RefreshTokenDto): AuthTokenDto {
        // 1. Refresh 토큰 검증
        if (!jwtTokenProvider.validateRefreshToken(dto.refreshToken)) {
            throw InvalidRefreshTokenException()
        }
        
        // 2. 토큰에서 이메일 추출
        val email = jwtTokenProvider.getUserEmailFromToken(dto.refreshToken)
        val user = getValidUserByEmail(email)
        
        // 3. Redis에서 저장된 Refresh 토큰 확인
        val storedTokens = refreshTokenRepository.findAllByEmail(email)
        if (storedTokens.isEmpty()) {
            throw InvalidRefreshTokenException()
        }
        
        // 4. 재사용 공격 방지 - 사용 횟수 확인
        val currentToken = storedTokens.firstOrNull { !it.isRevoked && !it.isExpired() }
            ?: throw InvalidRefreshTokenException()
            
        if (currentToken.usageCount >= 3) {
            // 너무 많이 사용된 토큰 - 보안 위협 가능성
            refreshTokenRepository.deleteAllByEmail(email)
            throw InvalidRefreshTokenException()
        }
        
        // 5. 기존 토큰 사용 횟수 증가
        val updatedToken = currentToken.incrementUsage()
        refreshTokenRepository.save(updatedToken)
        
        // 6. 새로운 Access Token 생성
        val newAccessToken = generateAccessToken(user.email)
        
        // 7. Refresh Token Rotation - 일정 사용 횟수 후 새로운 Refresh Token 발급
        if (updatedToken.usageCount >= 2) {
            // 기존 토큰 폐기
            refreshTokenRepository.delete(updatedToken)
            
            // 새로운 Refresh Token 발급
            val newRefreshToken = generateRefreshToken(user.email)
            val newRefreshTokenEntity = RefreshToken(
                id = UUID.randomUUID().toString(),
                email = user.email,
                ttl = 2592000 // 30일
            )
            refreshTokenRepository.save(newRefreshTokenEntity)
            
            return AuthTokenDto(newAccessToken, newRefreshToken)
        }
        
        // 8. 기존 Refresh Token 유지
        return AuthTokenDto(newAccessToken, dto.refreshToken.let { 
            JwtTokenDto(it, System.currentTimeMillis() + 2592000000) // 30일
        })
    }

    fun findValidUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw UserNotFoundException()
    }
    fun getValidUserByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw UserNotFoundException()
    }
    fun getValidUser(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException() }
    }

    fun getUserInfo(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw UserNotFoundException()
    }
    
    /**
     * 로그아웃 - 모든 Refresh 토큰 폐기
     */
    @Transactional
    fun logout(email: String) {
        // Redis에서 해당 사용자의 모든 Refresh 토큰 삭제
        refreshTokenRepository.deleteAllByEmail(email)
    }
    
    /**
     * 특정 디바이스의 Refresh 토큰만 폐기
     */
    @Transactional
    fun revokeRefreshToken(email: String, tokenId: String) {
        val token = refreshTokenRepository.findById(tokenId).orElse(null)
        if (token != null && token.email == email) {
            val revokedToken = token.revoke()
            refreshTokenRepository.save(revokedToken)
        }
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

    private fun User.toDto(): UserResponseDto {
        return UserResponseDto(
            id = this.id!!,
            email = this.email,
            userName = this.userName,
            nickname = this.nickname,
            profileMessage = this.profileMessage,
            profileImageLink = this.profileImageLink
        )
    }
}