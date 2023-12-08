package com.ssuspot.sns.domain.model.post.event

data class LikeDataStoreEvent(
    val userId: String,
    val postId: String,
    val rating: Double,
    val timestamp: Long,
    val tags: List<String>
)