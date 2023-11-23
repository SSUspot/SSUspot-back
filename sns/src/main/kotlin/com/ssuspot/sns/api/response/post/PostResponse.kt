package com.ssuspot.sns.api.response.post

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val nickname: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long
)