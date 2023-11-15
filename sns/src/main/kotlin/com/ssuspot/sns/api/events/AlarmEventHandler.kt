package com.ssuspot.sns.api.events

import com.ssuspot.sns.application.dto.alarm.CommentAlarmDto
import com.ssuspot.sns.application.service.alarm.CommentAlarmService
import com.ssuspot.sns.domain.model.alarm.event.CommentAlarmEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AlarmEventHandler(
    private val commentAlarmService: CommentAlarmService,
) {
    @EventListener
    @Async
    fun sendAlarm(event: CommentAlarmEvent) {
        val commentAlarmDto = CommentAlarmDto(
            postUserId = event.postUserId,
            postId = event.postId,
            commentUserId = event.commentUserId,
            commentId = event.commentId
        )
        commentAlarmService.saveCommentAlarm(commentAlarmDto)
    }
}