package com.ssuspot.sns.application.dto.post

data class CreatePostRequestDto(
    val title: String,
    val content: String,
    val userEmail: String,
    val imageUrls: String,
    val tags: List<String>,
    val spotId: Long,
)