package com.ssuspot.sns.api.response.post

import com.ssuspot.sns.api.response.user.UserResponse

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val user: UserResponse,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long,
    val createdAt: String,
    val updatedAt: String,
    val hasLiked: Boolean = false
)