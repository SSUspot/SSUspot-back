package com.ssuspot.sns.core.user.event.handler

import com.ssuspot.sns.core.user.event.RegisteredUserEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class RegisterUserEventHandler {
    //생성된 유저에 대해 타임라인 생성 요청
    @Async
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    fun createTimeline(event: RegisteredUserEvent) {
        println("create timeline for User")
    }

    //성공 메일 발송
    @Async
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    fun sendSuccessMail(event: RegisteredUserEvent) {
        println("sendSuccessMail")
    }
}