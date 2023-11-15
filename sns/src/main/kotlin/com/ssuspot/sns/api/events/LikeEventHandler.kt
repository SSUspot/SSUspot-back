package com.ssuspot.sns.api.events

import com.ssuspot.sns.domain.model.alarm.event.LikeEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class LikeEventHandler(

) {
    @EventListener
    @Async
    fun sendAlarm(event: LikeEvent) {
        //send to kafka producer
        println("send to kafka producer")
    }
}