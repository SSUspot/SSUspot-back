package com.ssuspot.sns.application.dto.post

data class GetFollowingPostsDto(
    val email: String,
    val page: Int,
    val size: Int,
)