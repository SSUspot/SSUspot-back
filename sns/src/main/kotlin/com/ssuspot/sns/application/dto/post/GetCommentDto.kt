package com.ssuspot.sns.application.dto.post

data class GetCommentDto(
    val postId: Long,
    val page: Int,
    val size: Int
)