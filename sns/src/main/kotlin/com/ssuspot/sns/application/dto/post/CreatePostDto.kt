package com.ssuspot.sns.application.dto.post

data class CreatePostDto(
    val title: String,
    val content: String,
    val userEmail: String,
    val imageUrls: List<String>,
    val spotId: Long,
)