package com.ssuspot.sns.application.dto.user

data class RegisterResponseDto (
    val id: Long,
    val email: String,
    val userName: String,
    val nickname: String,
    val profileMessage: String?,
    val profileImageLink: String?
)