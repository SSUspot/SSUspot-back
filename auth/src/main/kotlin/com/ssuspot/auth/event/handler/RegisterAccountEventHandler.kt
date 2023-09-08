package com.ssuspot.auth.event.handler

import com.ssuspot.auth.event.RegisteredAccountEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class RegisterAccountEventHandler {

    //feign을 통해 sns서버에 계정 생성 요청
    @Async
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    fun createSSUspotUser(event: RegisteredAccountEvent) {
        println("createSSUspotUser")
    }

    //성공 메일 발송
    @Async
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    fun sendSuccessMail(event: RegisteredAccountEvent) {
        println("sendSuccessMail")
    }
}