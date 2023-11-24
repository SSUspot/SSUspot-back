package com.ssuspot.sns.application.dto.post

import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.infrastructure.utils.time.EpochTimeUtil

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val nickname: String,
    val imageUrl: String,
    val tags: List<String>,
    val spotId: Long,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toDto(): PostResponse {
        return PostResponse(
            this.id,
            this.title,
            this.content,
            this.nickname,
            this.imageUrl,
            this.tags,
            this.spotId,
            createdAt = EpochTimeUtil.convertEpochTimeToLocalDateTime(this.createdAt.toLong()).toString(),
            updatedAt = EpochTimeUtil.convertEpochTimeToLocalDateTime(this.updatedAt.toLong()).toString()
        )
    }
}