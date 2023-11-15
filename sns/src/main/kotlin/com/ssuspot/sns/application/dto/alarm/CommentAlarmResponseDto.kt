package com.ssuspot.sns.application.dto.alarm

data class CommentAlarmResponseDto(
    val commentAlarmId: Long,
    val articleTitle: String,
    val commentContent: String,
)