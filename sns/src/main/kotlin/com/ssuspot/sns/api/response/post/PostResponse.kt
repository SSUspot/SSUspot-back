package com.ssuspot.sns.api.response.post

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long
)