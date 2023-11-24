package com.ssuspot.sns.application.dto.user

import com.ssuspot.sns.api.response.user.UserResponse

data class UserResponseDto (
    val id: Long,
    val email: String,
    val userName: String,
    val nickname: String,
    val profileMessage: String?,
    val profileImageLink: String?
) {
    fun toResponseDto(): UserResponse {
        return UserResponse(
            this.id,
            this.email,
            this.userName,
            this.nickname,
            this.profileMessage,
            this.profileImageLink
        )
    }
}