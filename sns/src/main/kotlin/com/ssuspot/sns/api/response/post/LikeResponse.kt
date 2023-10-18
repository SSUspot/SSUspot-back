package com.ssuspot.sns.api.response.post

data class LikeResponse(
    val likeId: Long,
    val postId: Long,
    val userNickname: String
)