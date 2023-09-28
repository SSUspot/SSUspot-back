package com.ssuspot.sns.application.dto.post

data class CommentResponseDto(
    val id: Long,
    val postId: Long,
    val userEmail: String,
    val content: String,
)