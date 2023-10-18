package com.ssuspot.sns.application.dto.post

data class LikeResponseDto(
    val id: Long,
    val postId: Long,
    val userNickname: String
)