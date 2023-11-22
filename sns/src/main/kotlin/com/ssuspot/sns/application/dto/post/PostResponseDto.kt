package com.ssuspot.sns.application.dto.post

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val nickname: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long,
)