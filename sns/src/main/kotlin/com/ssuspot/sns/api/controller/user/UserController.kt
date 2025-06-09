package com.ssuspot.sns.api.controller.user

import com.ssuspot.sns.api.response.common.ApiResponse
import com.ssuspot.sns.api.response.user.FollowUserResponse
import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RefreshRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.request.user.UpdateUserDataRequest
import com.ssuspot.sns.api.response.user.LoginResponse
import com.ssuspot.sns.api.response.user.UserInfoResponse
import com.ssuspot.sns.api.response.user.UserResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.user.*
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "User", description = "사용자 관리 API")
class UserController(
    val userService: UserService
) {
    @GetMapping("/api/users/following")
    @Operation(
        summary = "내 팔로잉 목록 조회",
        description = "현재 사용자가 팔로우하고 있는 사용자 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "팔로잉 목록 조회 성공"
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    ])
    fun getMyFollowingList(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
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

    @GetMapping("/api/users/{userId}")
    fun getUserInfo(
        @PathVariable("userId") userId: Long,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<UserInfoResponse> {
        val user = userService.getSpecificUser(userId, authInfo.email)
        return ResponseEntity.ok().body(
            UserInfoResponse(
                user.id,
                user.email,
                user.userName,
                user.nickname,
                user.profileMessage,
                user.profileImageLink,
                user.followed
            )
        )
    }

    @PostMapping("/api/users/register")
    @Operation(
        summary = "회원가입",
        description = "새로운 사용자 계정을 생성합니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "회원가입 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (이메일 중복, 필수 필드 누락 등)"
        )
    ])
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
    @Operation(
        summary = "로그인",
        description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = LoginResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패 (잘못된 이메일 또는 비밀번호)"
        )
    ])
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
    
    @PostMapping("/api/users/logout")
    fun logout(
        @Auth authInfo: AuthInfo
    ): ResponseEntity<Unit> {
        userService.logout(authInfo.email)
        return ResponseEntity.ok().build()
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