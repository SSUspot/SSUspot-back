package com.ssuspot.sns.application.dto.post

data class GetRecommendedPostsDto(
    val email: String,
    val page: Int,
    val size: Int
)