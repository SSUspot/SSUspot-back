package com.ssuspot.sns.domain.model.alarm.event

class LikeEvent(
    val postUserId: Long,
    val postId: Long,
    val likeUserId: Long,
)