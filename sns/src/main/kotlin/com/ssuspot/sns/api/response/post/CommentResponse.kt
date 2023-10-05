package com.ssuspot.sns.api.response.post

data class CommentResponse(
    val id: Long,
    val postId: Long,
    val nickname: String,
    val content: String,
)