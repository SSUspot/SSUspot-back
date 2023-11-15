package com.ssuspot.sns.domain.model.alarm.event

class AlarmEvent(
    val postUserId: Long,
    val postId: Long,
    val commentUserId: Long,
    val commentId: Long,
)