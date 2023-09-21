package com.ssuspot.sns.application.dto.post

data class GetUserPostsDto(
    val page: Int,
    val size: Int,
    val sort: String,
    val userId: Long
)