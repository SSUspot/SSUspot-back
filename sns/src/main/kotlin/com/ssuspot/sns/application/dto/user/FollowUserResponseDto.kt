package com.ssuspot.sns.application.dto.user

import com.ssuspot.sns.api.response.user.FollowUserResponse

data class FollowUserResponseDto(
        val id: Long,
        val userId: Long,
        val nickname: String,
        val userName: String,
        val profileImageLink: String?
) {
        fun toDto(): FollowUserResponse {
                return FollowUserResponse(
                        id = this.id,
                        userId = this.userId,
                        nickname = this.nickname,
                        userName = this.userName,
                        profileImageLink = this.profileImageLink
                )
        }
}