package com.ssuspot.sns.api.controller.user

import com.ssuspot.sns.api.response.user.FollowUserResponse
import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RefreshRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.request.user.UpdateUserDataRequest
import com.ssuspot.sns.api.response.user.LoginResponse
import com.ssuspot.sns.api.response.user.UserResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.user.*
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    val userService: UserService
) {
    @GetMapping("/api/users/following")
    fun getMyFollowingList(
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<FollowUserResponse>> {
        val followingList = userService.getMyFollowingList(authInfo.email)
        return ResponseEntity.ok().body(
            followingList.map {
                it.toDto()
            }
        )
    }

    @GetMapping("/api/users/{userId}/following")
    fun getFollowingListOfUser(
        @Auth authInfo: AuthInfo,
        @PathVariable("userId") userId: Long
    ): ResponseEntity<List<FollowUserResponse>> {
        val followingList = userService.getFollowingListOfUser(userId)
        return ResponseEntity.ok().body(
            followingList.map {
                it.toDto()
            }
        )
    }

    @GetMapping("/api/users/follower")
    fun getMyFollowerList(
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<FollowUserResponse>> {
        val followerList = userService.getMyFollowerList(authInfo.email)
        return ResponseEntity.ok().body(
            followerList.map {
                it.toDto()
            }
        )
    }

    @GetMapping("/api/users/{userId}/follower")
    fun getFollowerListOfUser(
        @Auth authInfo: AuthInfo,
        @PathVariable("userId") userId: Long
    ): ResponseEntity<List<FollowUserResponse>> {
        val followerList = userService.getFollowerListOfUser(userId)
        return ResponseEntity.ok().body(
            followerList.map {
                it.toDto()
            }
        )
    }

    @GetMapping("/api/users")
    fun getUserInfo(
        @Auth authInfo: AuthInfo
    ): ResponseEntity<UserResponse> {
        val user = userService.getUserInfo(authInfo.email)
        return ResponseEntity.ok().body(
            UserResponse(
                user.id!!,
                user.email,
                user.userName,
                user.nickname,
                user.profileMessage,
                user.profileImageLink
            )
        )
    }

    @PostMapping("/api/users/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): ResponseEntity<UserResponse> {
        val savedUser = userService.registerProcess(
            RegisterDto(
                request.email,
                request.password,
                request.userName,
                request.nickname,
                request.profileMessage,
                request.profileImageLink
            )
        )
        return ResponseEntity.ok().body(
            UserResponse(
                savedUser.id,
                savedUser.email,
                savedUser.userName,
                savedUser.nickname,
                savedUser.profileMessage,
                savedUser.profileImageLink
            )
        )
    }

    @PostMapping("/api/users/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {
        val token = userService.login(
            LoginDto(
                request.email,
                request.password
            )
        )
        return ResponseEntity.ok(
            LoginResponse(token)
        )
    }

    @PostMapping("/api/users/refresh")
    fun refresh(
        @RequestBody request: RefreshRequest
    ): ResponseEntity<LoginResponse> {
        val token = userService.refresh(
            RefreshTokenDto(
                request.refreshToken
            )
        )
        return ResponseEntity.ok(
            LoginResponse(token)
        )
    }

    @PostMapping("/api/users/following/{userId}")
    fun follow(
        @PathVariable("userId") userId: Long,
        @Auth authInfo: AuthInfo,
    ): ResponseEntity<FollowUserResponse> {
        val user = userService.follow(
            FollowingRequestDto(
                authInfo.email,
                userId
            )
        )
        return ResponseEntity.ok().body(
            user.toDto()
        )
    }

    //unfollowing
    @DeleteMapping("/api/users/following/{userId}")
    fun unfollow(
        @PathVariable("userId") userId: Long,
        @Auth authInfo: AuthInfo,
    ) {
        userService.unfollow(
            FollowingRequestDto(
                authInfo.email,
                userId
            )
        )
    }

    @PatchMapping("/api/users")
    fun updateUserProfile(
        @Auth authInfo: AuthInfo,
        @RequestBody request: UpdateUserDataRequest
    ): ResponseEntity<UserResponse> {
        val user = userService.updateProfile(
            UpdateUserDataDto(
                authInfo.email,
                request.userName,
                request.nickname,
                request.profileMessage,
                request.profileImageLink
            )
        )
        return ResponseEntity.ok().body(
            user.toResponseDto()
        )
    }
}