package com.ssuspot.sns.api.response.post

class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val imageUrls: List<String>,
    val spotId: Long
)