package com.ssuspot.sns.application.dto.user

class RegisterDto(
        val email: String,
        val password: String,
        val userName: String,
        val nickname: String,
        val profileMessage: String?,
        val profileImageLink: String?
)