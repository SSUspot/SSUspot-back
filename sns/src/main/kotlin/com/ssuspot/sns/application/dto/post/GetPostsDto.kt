package com.ssuspot.sns.application.dto.post

data class GetPostsDto(
    val page: Int,
    val size: Int,
    val email: String
)