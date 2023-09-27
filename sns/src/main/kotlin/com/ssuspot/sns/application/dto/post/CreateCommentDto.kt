package com.ssuspot.sns.application.dto.post

data class CreateCommentDto(
    val postId: Long,
    val userEmail: String,
    val content: String,
)