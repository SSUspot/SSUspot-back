package com.ssuspot.sns.application.dto.post

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val email: String,
    val imageUrls: List<String>,
    val spotId: Long,
)