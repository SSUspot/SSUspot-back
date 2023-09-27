package com.ssuspot.sns.application.dto.post

class CommentResponseDto(
    val id: Long,
    val postId: Long,
    val userEmail: String,
    val content: String,
)