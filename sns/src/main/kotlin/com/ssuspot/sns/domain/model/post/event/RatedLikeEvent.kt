package com.ssuspot.sns.domain.model.post.event

data class RatedLikeEvent(
    val userId: String,
    val postId: String,
    val rating: Double,
    val timestamp: Long
)
