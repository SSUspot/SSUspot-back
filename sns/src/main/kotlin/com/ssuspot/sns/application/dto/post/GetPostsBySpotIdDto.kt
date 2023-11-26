package com.ssuspot.sns.application.dto.post

data class GetPostsBySpotIdDto(
    val spotId: Long,
    val email: String,
    val page: Int,
    val size: Int,
)