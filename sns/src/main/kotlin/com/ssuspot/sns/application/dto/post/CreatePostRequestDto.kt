package com.ssuspot.sns.application.dto.post

data class CreatePostRequestDto(
    val title: String,
    val content: String,
    val userEmail: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long,
)