package com.ssuspot.sns.api.request.post

data class CreatePostRequest(
    val title: String,
    val content: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long,
)