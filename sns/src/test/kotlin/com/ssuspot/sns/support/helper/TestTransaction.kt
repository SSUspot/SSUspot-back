package com.ssuspot.sns.support.helper

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TestTransaction {
    
    @Transactional
    fun <T> execute(action: () -> T): T {
        return action()
    }
    
    @Transactional(readOnly = true)
    fun <T> executeReadOnly(action: () -> T): T {
        return action()
    }
}