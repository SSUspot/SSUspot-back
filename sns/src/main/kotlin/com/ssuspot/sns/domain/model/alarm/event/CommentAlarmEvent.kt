package com.ssuspot.sns.domain.model.alarm.event

class CommentAlarmEvent(
    val postUserId: Long,
    val postId: Long,
    val commentUserId: Long,
    val commentId: Long,
)