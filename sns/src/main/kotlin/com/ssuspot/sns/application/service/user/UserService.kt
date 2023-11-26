package com.ssuspot.sns.application.service.user

import com.ssuspot.sns.application.dto.common.JwtTokenDto
import com.ssuspot.sns.application.dto.user.*
import com.ssuspot.sns.domain.exceptions.user.AlreadyFollowedUserException
import com.ssuspot.sns.domain.model.user.event.RegisteredUserEvent
import com.ssuspot.sns.domain.exceptions.user.EmailExistException
import com.ssuspot.sns.domain.exceptions.user.UserNotFoundException
import com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.user.entity.UserFollow
import com.ssuspot.sns.infrastructure.aop.CacheUser
import com.ssuspot.sns.infrastructure.repository.post.UserFollowRepository
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userFollowRepository: UserFollowRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    @Transactional
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
        val user = userRepository.findByEmail(loginDto.email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(loginDto.password, user.password)) throw UserPasswordIncorrectException()

        //refresh,access token 생성
        val accessToken = generateAccessToken(user.email)
        val refreshToken = generateRefreshToken(user.email)
        return AuthTokenDto(accessToken, refreshToken)
    }

    @Transactional
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
        return FollowUserResponseDto(
            id = userFollow.id!!,
            userId = userFollow.followedUser.id!!,
            userName = userFollow.followedUser.userName,
            nickname = userFollow.followedUser.nickname,
            profileImageLink = userFollow.followedUser.profileImageLink
        )
    }

    @Transactional
    fun unfollow(
        followingRequestDto: FollowingRequestDto
    ){
        val user = getValidUserByEmail(followingRequestDto.email)
        val followedUser = getValidUser(followingRequestDto.userId)
        val userFollow = userFollowRepository.findByFollowingUserAndFollowedUser(user, followedUser) ?: throw UserNotFoundException()
        userFollowRepository.delete(userFollow)
    }

    @Transactional
    fun getMyFollowingList(email: String): List<FollowUserResponseDto>{
        val user = getValidUserByEmail(email)
        return user.followers.map {
            FollowUserResponseDto(
                id = it.id!!,
                userId = it.followedUser.id!!,
                userName = it.followedUser.userName,
                nickname = it.followedUser.nickname,
                profileImageLink = it.followedUser.profileImageLink
            )
        }
    }

    @Transactional
    fun getFollowingListOfUser(userId:Long): List<FollowUserResponseDto>{
        val user = getValidUser(userId)
        return user.followers.map {
            FollowUserResponseDto(
                id = it.id!!,
                userId = it.followedUser.id!!,
                userName = it.followedUser.userName,
                nickname = it.followedUser.nickname,
                profileImageLink = it.followedUser.profileImageLink
            )
        }
    }

    @Transactional
    fun getMyFollowerList(email: String): List<FollowUserResponseDto>{
        val user = getValidUserByEmail(email)
        return user.following.map {
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
    fun getFollowerListOfUser(userId:Long): List<FollowUserResponseDto>{
        val user = getValidUser(userId)
        return user.following.map {
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
        val email = jwtTokenProvider.getUserEmailFromToken(dto.refreshToken)
        val user = getValidUserByEmail(email)
        val accessToken = generateAccessToken(user.email)
        val refreshToken = generateRefreshToken(user.email)

        // TODO: refresh token 캐시에 업데이트 하는 코드 작성 필요

        return AuthTokenDto(accessToken, refreshToken)
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