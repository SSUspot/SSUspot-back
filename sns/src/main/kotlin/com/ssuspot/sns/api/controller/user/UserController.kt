package com.ssuspot.sns.api.controller.user

import com.ssuspot.sns.application.dto.user.LoginDto
import com.ssuspot.sns.application.dto.user.RegisterDto
import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.response.user.LoginResponse
import com.ssuspot.sns.api.response.user.RegisterResponse
import com.ssuspot.sns.application.service.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController (
        val userService: UserService
){
    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ):ResponseEntity<RegisterResponse>{
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
            RegisterResponse(
                    savedUser.id,
                    savedUser.email,
                    savedUser.userName,
                    savedUser.nickname,
                    savedUser.profileMessage,
                    savedUser.profileImageLink
            )
        )
    }

    @PostMapping("/login")
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