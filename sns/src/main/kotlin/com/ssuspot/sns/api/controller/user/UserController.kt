package com.ssuspot.sns.api.controller.user

import com.ssuspot.sns.application.dto.user.LoginDto
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.response.user.LoginResponse
import com.ssuspot.sns.api.response.user.UserResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController (
        val userService: UserService
){
    @GetMapping("/api/users/")
    fun getUserInfo(
        @Auth authInfo: AuthInfo
    ): ResponseEntity<UserResponse>{
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
    ):ResponseEntity<UserResponse>{
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
    ):ResponseEntity<LoginResponse>{
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
}