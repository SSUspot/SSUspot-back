package com.ssuspot.sns.api.response.alarm

data class AlarmResponse(
    val alarmId: Long,
    val commentedUser: String,
    val articleTitle: String,
    val commentContent: String,
)