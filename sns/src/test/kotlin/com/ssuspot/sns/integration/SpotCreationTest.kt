package com.ssuspot.sns.integration

import com.ssuspot.sns.api.request.spot.CreateSpotRequest
import com.ssuspot.sns.infrastructure.configs.TestCacheConfig
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
@DisplayName("스팟 생성 통합 테스트")
class SpotCreationTest {

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    @DisplayName("정상적인 스팟 생성이 성공한다")
    fun createSpot_success() {
        // given
        val timestamp = System.currentTimeMillis()
        val createSpotRequest = CreateSpotRequest(
            spotName = "테스트 카페 ${timestamp}",
            spotThumbnailImageLink = "https://example.com/cafe.jpg",
            spotAddress = "서울시 강남구 테헤란로 123",
            spotInfo = "분위기 좋은 카페입니다.",
            spotLevel = 3,
            latitude = 37.5665,
            longitude = 126.9780
        )

        // when & then
        given()
            .contentType(ContentType.JSON)
            .body(createSpotRequest)
        .`when`()
            .post("/api/spots")
        .then()
            .statusCode(200)
    }
}