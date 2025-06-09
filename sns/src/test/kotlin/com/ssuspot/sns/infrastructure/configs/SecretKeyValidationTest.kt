package com.ssuspot.sns.infrastructure.configs

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

/**
 * Spring 컨텍스트와 함께 실행되는 SecretKeyConfig 통합 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
class SecretKeyValidationTest {
    
    @Autowired
    private lateinit var secretKeyConfig: SecretKeyConfig
    
    @Test
    fun `테스트 환경에서 SecretKeyConfig가 정상적으로 로드된다`() {
        // 테스트 환경에서는 검증이 스킵되므로 예외가 발생하지 않아야 함
        // @PostConstruct가 이미 실행되었으므로 별도 검증 불필요
    }
}