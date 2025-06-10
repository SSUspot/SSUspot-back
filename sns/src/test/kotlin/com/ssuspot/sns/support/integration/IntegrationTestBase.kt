package com.ssuspot.sns.support.integration

import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * 통합 테스트 기반 클래스
 * TestContainers를 사용하여 실제 DB와 Redis 환경에서 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
abstract class IntegrationTestBase {

    @LocalServerPort
    protected var port: Int = 0

    companion object {
        @Container
        @JvmStatic
        val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
            withDatabaseName("ssuspot_test")
            withUsername("test")
            withPassword("test")
            withReuse(true)
        }

        @Container
        @JvmStatic
        val redisContainer = GenericContainer<Nothing>("redis:7-alpine").apply {
            withExposedPorts(6379)
            withReuse(true)
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            // PostgreSQL 설정
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)

            // Redis 설정
            registry.add("spring.redis.host", redisContainer::getHost)
            registry.add("spring.redis.port") { redisContainer.getMappedPort(6379) }
            registry.add("spring.redis.password") { "" }

            // JWT 설정 (테스트용)
            registry.add("app.jwt.secret") { "test-jwt-secret-key-for-integration-tests-must-be-at-least-32-characters-long" }
            registry.add("app.jwt.accessTokenExpirationMS") { "3600000" } // 1시간
            registry.add("app.jwt.refreshTokenExpirationMS") { "86400000" } // 24시간

            // AWS S3 설정 (테스트용 더미)
            registry.add("cloud.aws.s3.bucket") { "test-bucket" }
            registry.add("cloud.aws.credentials.access-key") { "test-access-key" }
            registry.add("cloud.aws.credentials.secret-key") { "test-secret-key" }
            registry.add("cloud.aws.region.static") { "ap-northeast-2" }

            // 로깅 레벨 설정
            registry.add("logging.level.org.hibernate.SQL") { "debug" }
            registry.add("logging.level.com.ssuspot.sns") { "debug" }
        }
    }

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

    /**
     * 테스트 간 데이터 격리를 위한 메소드
     * 필요시 하위 클래스에서 오버라이드
     */
    protected open fun cleanUp() {
        // 기본적으로는 @Transactional + @Rollback으로 처리
        // 필요시 하위 클래스에서 직접 데이터 정리 로직 구현
    }
}