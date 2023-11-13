package com.ssuspot.sns.application.dto.alarm

data class AlarmDto(
    val postUserId: Long,
    val postId: Long,
    val commentUserId: Long,
    val commentId: Long,
)