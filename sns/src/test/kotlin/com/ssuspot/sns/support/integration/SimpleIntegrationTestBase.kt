package com.ssuspot.sns.support.integration

import com.ssuspot.sns.infrastructure.configs.TestCacheConfig
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

/**
 * 간단한 통합 테스트 기반 클래스
 * H2 인메모리 DB를 사용하여 빠른 테스트 실행
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Import(TestCacheConfig::class)
abstract class SimpleIntegrationTestBase {

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        
        // JSON 직렬화 설정
        RestAssured.config = RestAssuredConfig.config()
            .objectMapperConfig(
                ObjectMapperConfig.objectMapperConfig()
                    .jackson2ObjectMapperFactory { _, _ ->
                        com.fasterxml.jackson.databind.ObjectMapper().apply {
                            registerModule(com.fasterxml.jackson.module.kotlin.kotlinModule())
                            configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        }
                    }
            )
    }
}