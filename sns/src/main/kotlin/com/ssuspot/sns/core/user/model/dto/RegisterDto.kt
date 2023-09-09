package com.ssuspot.sns.core.user.model.dto

class RegisterDto(
        val email: String,
        val password: String,
        val userName: String,
        val nickname: String,
        val profileMessage: String?,
        val profileImageLink: String?
)