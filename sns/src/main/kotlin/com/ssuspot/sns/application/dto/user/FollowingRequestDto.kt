package com.ssuspot.sns.application.dto.user

data class FollowingRequestDto(
    val email: String,
    val userId: Long,
)