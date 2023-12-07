package com.ssuspot.sns.application.dto.post

import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.domain.model.user.entity.User

data class CreatePostRequestDto(
    val title: String,
    val content: String,
    val userEmail: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val spotId: Long,
) {
    fun toEntity(spot: Spot, user: User): Post {
        return Post(
            title = this.title,
            content = this.content,
            user = user,
            spot = spot,
            imageUrls = this.imageUrls
        )
    }
}