package com.ssuspot.sns.integration

import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.infrastructure.configs.TestCacheConfig
import com.ssuspot.sns.support.integration.AuthTestHelper
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import io.restassured.RestAssured

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Import(TestCacheConfig::class)
@DisplayName("사용자 등록 통합 테스트")
class UserRegistrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    @DisplayName("정상적인 회원가입이 성공한다")
    fun registerUser_success() {
        // given
        val timestamp = System.currentTimeMillis()
        val registerRequest = RegisterRequest(
            email = "test${timestamp}@example.com",
            password = "Password123!",
            userName = "user${timestamp}",
            nickname = "테스트유저${timestamp}",
            profileMessage = "테스트 프로필",
            profileImageLink = null
        )

        // when & then
        given()
            .contentType(ContentType.JSON)
            .body(registerRequest)
        .`when`()
            .post("/api/users/register")
        .then()
            .statusCode(200)
    }
}