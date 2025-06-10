package com.ssuspot.sns

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName

@DisplayName("간단한 테스트")
class SimpleTest {
    
    @Test
    @DisplayName("기본 테스트가 작동한다")
    fun basicTest() {
        assertEquals(2 + 2, 4)
    }
}