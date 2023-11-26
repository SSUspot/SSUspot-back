package com.ssuspot.sns.api.response.user

data class UserInfoResponse(
    val id: Long,
    val email: String,
    val userName: String,
    val nickname: String,
    val profileMessage: String?,
    val profileImageLink: String?,
    val followed: Boolean,
)