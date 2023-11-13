package com.ssuspot.sns.application.dto.alarm

data class AlarmResponseDto(
    val alarmId: Long,
    val articleTitle: String,
    val commentContent: String,
)