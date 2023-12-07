package com.ssuspot.sns.api.response.post

import com.ssuspot.sns.api.response.user.UserResponse

data class CommentResponse(
    val id: Long,
    val postId: Long,
    val user: UserResponse,
    val content: String,
)