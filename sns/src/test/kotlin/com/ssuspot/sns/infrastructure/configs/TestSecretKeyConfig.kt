package com.ssuspot.sns.infrastructure.configs

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

/**
 * 테스트용 SecretKeyConfig
 * 실제 환경 변수 검증을 수행하지 않음
 */
@TestConfiguration
@Profile("test")
class TestSecretKeyConfig {
    
    @Bean
    fun secretKeyConfig(): SecretKeyConfig {
        // 테스트용 고정 값 사용
        return SecretKeyConfig(
            "test-secret-key-for-jwt-token-generation-must-be-at-least-32-characters-long",
            "",
            "test"
        )
    }
}