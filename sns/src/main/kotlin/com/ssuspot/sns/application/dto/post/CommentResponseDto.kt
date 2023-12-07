package com.ssuspot.sns.application.dto.post

import com.ssuspot.sns.api.response.post.CommentResponse
import com.ssuspot.sns.application.dto.user.UserResponseDto

data class CommentResponseDto(
    val id: Long,
    val postId: Long,
    val user: UserResponseDto,
    val content: String,
) {
    fun toResponseDto(): CommentResponse =
        CommentResponse(
            id = id,
            postId = postId,
            user = user.toResponseDto(),
            content = content,
        )
}