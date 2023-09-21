package com.ssuspot.sns.application.dto.post

data class GetMyPostsDto(
    val page: Int,
    val size: Int,
    val email: String
)