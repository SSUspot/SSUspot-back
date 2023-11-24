package com.ssuspot.sns.api.request.post

data class CreatePostRequest(
    val title: String,
    val content: String,
    val imageUrl: String,
    val tags: List<String>,
    val spotId: Long,
)