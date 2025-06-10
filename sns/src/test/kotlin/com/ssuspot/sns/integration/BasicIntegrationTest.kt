package com.ssuspot.sns.integration

import com.ssuspot.sns.infrastructure.configs.TestCacheConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestCacheConfig::class)
@DisplayName("기본 통합 테스트")
class BasicIntegrationTest {

    @Test
    @DisplayName("Spring 컨텍스트가 정상적으로 로드된다")
    fun contextLoads() {
        // Spring 컨텍스트가 성공적으로 로드되면 테스트 통과
    }
}