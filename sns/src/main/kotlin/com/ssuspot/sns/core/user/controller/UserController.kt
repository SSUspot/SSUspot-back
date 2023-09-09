package com.ssuspot.sns.core.user.controller

import com.ssuspot.sns.core.user.model.dto.RegisterDto
import com.ssuspot.sns.core.user.request.UserCreateRequest
import com.ssuspot.sns.core.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController (
        val userService: UserService
){
    @PostMapping
    fun createUser(
        @RequestBody request: UserCreateRequest
    ){
        userService.createUser(
                RegisterDto(
                        request.email,
                        request.password,
                        request.userName,
                        request.nickname,
                        request.profileMessage,
                        request.profileImageLink
                )
        )
    }
}