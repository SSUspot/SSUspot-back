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
@Tag(name = "User", description = "사용자 관리 API - 회원가입, 로그인, 프로필 관리, 팔로우/팔로워 기능을 제공합니다.")
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
    @Operation(
        summary = "특정 사용자의 팔로잉 목록 조회",
        description = "특정 사용자가 팔로우하고 있는 사용자 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "팔로잉 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = FollowUserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getFollowingListOfUser(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo,
        @Parameter(description = "조회할 사용자 ID", required = true, example = "1")
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
    @Operation(
        summary = "내 팔로워 목록 조회",
        description = "현재 사용자를 팔로우하고 있는 사용자 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "팔로워 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = FollowUserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getMyFollowerList(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<FollowUserResponse>> {
        val followerList = userService.getMyFollowerList(authInfo.email)
        return ResponseEntity.ok().body(
            followerList.map {
                it.toDto()
            }
        )
    }

    @GetMapping("/api/users/{userId}/follower")
    @Operation(
        summary = "특정 사용자의 팔로워 목록 조회",
        description = "특정 사용자를 팔로우하고 있는 사용자 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "팔로워 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = FollowUserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getFollowerListOfUser(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo,
        @Parameter(description = "조회할 사용자 ID", required = true, example = "1")
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
    @Operation(
        summary = "현재 사용자 정보 조회",
        description = "로그인한 사용자의 상세 정보를 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "사용자 정보 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getUserInfo(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
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
    @Operation(
        summary = "특정 사용자 정보 조회",
        description = "특정 사용자의 상세 정보를 조회합니다. 팔로우 여부 정보도 포함됩니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "사용자 정보 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserInfoResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getUserInfo(
        @Parameter(description = "조회할 사용자 ID", required = true, example = "1")
        @PathVariable("userId") userId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
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
    @Operation(
        summary = "토큰 갱신",
        description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "토큰 갱신 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = LoginResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "유효하지 않은 Refresh Token",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun refresh(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "토큰 갱신 요청 정보",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = RefreshRequest::class))]
        )
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
    @Operation(
        summary = "로그아웃",
        description = "현재 사용자를 로그아웃 처리합니다. 서버에서 토큰을 무효화합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공"
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun logout(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<Unit> {
        userService.logout(authInfo.email)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/api/users/following/{userId}")
    @Operation(
        summary = "사용자 팔로우",
        description = "특정 사용자를 팔로우합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "팔로우 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = FollowUserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (이미 팔로우한 사용자, 자기 자신을 팔로우 등)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "팔로우할 사용자를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun follow(
        @Parameter(description = "팔로우할 사용자 ID", required = true, example = "1")
        @PathVariable("userId") userId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo,
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

    @DeleteMapping("/api/users/following/{userId}")
    @Operation(
        summary = "사용자 언팔로우",
        description = "특정 사용자의 팔로우를 취소합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "언팔로우 성공"
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (팔로우하지 않은 사용자)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun unfollow(
        @Parameter(description = "언팔로우할 사용자 ID", required = true, example = "1")
        @PathVariable("userId") userId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo,
    ) {
        userService.unfollow(
            FollowingRequestDto(
                authInfo.email,
                userId
            )
        )
    }

    @PatchMapping("/api/users")
    @Operation(
        summary = "사용자 프로필 수정",
        description = "현재 사용자의 프로필 정보를 수정합니다. 수정하고자 하는 필드만 포함하면 됩니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "프로필 수정 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 데이터)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun updateUserProfile(
        @Parameter(hidden = true) @Auth authInfo: AuthInfo,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "수정할 프로필 정보",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UpdateUserDataRequest::class))]
        )
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