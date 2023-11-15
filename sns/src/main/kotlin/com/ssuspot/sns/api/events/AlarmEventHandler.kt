package com.ssuspot.sns.api.events

import com.ssuspot.sns.application.dto.alarm.AlarmDto
import com.ssuspot.sns.application.service.alarm.AlarmService
import com.ssuspot.sns.domain.model.alarm.event.AlarmEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AlarmEventHandler(
    private val alarmService: AlarmService,
) {
    @EventListener
    @Async
    fun sendAlarm(event: AlarmEvent) {
        val alarmDto = AlarmDto(
            postUserId = event.postUserId,
            postId = event.postId,
            commentUserId = event.commentUserId,
            commentId = event.commentId
        )
        alarmService.saveAlarm(alarmDto)
    }
}