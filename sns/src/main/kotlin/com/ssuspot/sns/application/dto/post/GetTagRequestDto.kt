package com.ssuspot.sns.application.dto.post

data class GetTagRequestDto(
    val page: Int,
    val size: Int,
    val tagName: String
)