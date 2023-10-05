package com.ssuspot.sns.application.dto.post

data class CommentResponseDto(
    val id: Long,
    val postId: Long,
    val nickname: String,
    val content: String,
)