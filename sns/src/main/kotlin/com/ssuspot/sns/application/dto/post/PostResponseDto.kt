package com.ssuspot.sns.application.dto.post

import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.dto.user.UserResponseDto
import com.ssuspot.sns.infrastructure.utils.time.EpochTimeUtil

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val user: UserResponseDto,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long,
    val createdAt: String,
    val updatedAt: String,
    var hasLiked: Boolean = false
) {
    fun toResponseDto(): PostResponse {
        return PostResponse(
            this.id,
            this.title,
            this.content,
            this.user.toResponseDto(),
            this.imageUrls,
            this.tags,
            this.spotId,
            createdAt = EpochTimeUtil.convertEpochTimeToLocalDateTime(this.createdAt.toLong()).toString(),
            updatedAt = EpochTimeUtil.convertEpochTimeToLocalDateTime(this.updatedAt.toLong()).toString(),
            this.hasLiked
        )
    }
}