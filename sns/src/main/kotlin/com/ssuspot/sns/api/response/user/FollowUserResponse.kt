package com.ssuspot.sns.api.response.user

class FollowUserResponse(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val userName: String,
    val profileImageLink: String?
)