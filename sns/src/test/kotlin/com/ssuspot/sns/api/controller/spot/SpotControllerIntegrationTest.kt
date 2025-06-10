package com.ssuspot.sns.api.controller.spot

import com.ssuspot.sns.api.request.spot.CreateSpotRequest
import com.ssuspot.sns.api.response.spot.SpotResponse
import com.ssuspot.sns.support.integration.AuthTestHelper
import com.ssuspot.sns.support.integration.SimpleIntegrationTestBase
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@DisplayName("SpotController 통합 테스트")
@Transactional
class SpotControllerIntegrationTest : SimpleIntegrationTestBase() {

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @Nested
    @DisplayName("스팟 생성 API")
    inner class CreateSpotTest {

        @Test
        @DisplayName("정상적인 스팟 생성이 성공한다")
        fun createSpot_success() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "테스트 카페",
                spotThumbnailImageLink = "https://example.com/cafe.jpg",
                spotAddress = "서울시 강남구 테헤란로 123",
                spotInfo = "분위기 좋은 카페입니다. 조용하고 Wi-Fi가 잘 됩니다.",
                spotLevel = 3,
                latitude = 37.5665,
                longitude = 126.9780
            )

            // when & then
            val response = given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(200)
                .extract()
                .`as`(SpotResponse::class.java)

            assertThat(response.spotName).isEqualTo("테스트 카페")
            assertThat(response.spotAddress).isEqualTo("서울시 강남구 테헤란로 123")
            assertThat(response.spotLevel).isEqualTo(3)
            assertThat(response.latitude).isEqualTo(37.5665)
            assertThat(response.longitude).isEqualTo(126.9780)
        }

        @Test
        @DisplayName("필수 필드 누락 시 스팟 생성이 실패한다")
        fun createSpot_missingFields_fails() {
            // given
            val invalidRequest = mapOf(
                "spotName" to "이름만 있는 스팟"
                // spotAddress, spotInfo, spotLevel, latitude, longitude 누락
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(400)
        }

        @Test
        @DisplayName("잘못된 좌표로 스팟 생성 시 실패한다")
        fun createSpot_invalidCoordinates_fails() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "잘못된 좌표 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "잘못된 주소",
                spotInfo = "잘못된 좌표 테스트",
                spotLevel = 1,
                latitude = 999.0, // 잘못된 위도
                longitude = 999.0  // 잘못된 경도
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(400)
        }

        @Test
        @DisplayName("잘못된 스팟 레벨로 생성 시 실패한다")
        fun createSpot_invalidSpotLevel_fails() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "잘못된 레벨 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "잘못된 레벨 테스트",
                spotLevel = -1, // 잘못된 레벨
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
                .statusCode(400)
        }

        @Test
        @DisplayName("빈 스팟 이름으로 생성 시 실패한다")
        fun createSpot_emptySpotName_fails() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "", // 빈 이름
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "빈 이름 테스트",
                spotLevel = 1,
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
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("스팟 조회 API")
    inner class GetSpotTest {

        @Test
        @DisplayName("모든 스팟 목록 조회가 성공한다")
        fun getAllSpots_success() {
            // when & then
            given()
            .`when`()
                .get("/api/spots")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("특정 스팟 조회가 성공한다")
        fun getSpecificSpot_success() {
            // given - 먼저 스팟을 생성
            val createSpotRequest = CreateSpotRequest(
                spotName = "조회 테스트 스팟",
                spotThumbnailImageLink = "https://example.com/test.jpg",
                spotAddress = "서울시 테스트구",
                spotInfo = "조회 테스트용 스팟입니다",
                spotLevel = 2,
                latitude = 37.5665,
                longitude = 126.9780
            )

            val createdSpot = given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(200)
                .extract()
                .`as`(SpotResponse::class.java)

            // when & then
            val response = given()
            .`when`()
                .get("/api/spots/${createdSpot.id}")
            .then()
                .statusCode(200)
                .extract()
                .`as`(SpotResponse::class.java)

            assertThat(response.id).isEqualTo(createdSpot.id)
            assertThat(response.spotName).isEqualTo("조회 테스트 스팟")
        }

        @Test
        @DisplayName("존재하지 않는 스팟 조회 시 실패한다")
        fun getSpecificSpot_notFound_fails() {
            // given
            val nonExistentSpotId = 99999L

            // when & then
            given()
            .`when`()
                .get("/api/spots/$nonExistentSpotId")
            .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("스팟 유효성 검증 테스트")
    inner class SpotValidationTest {

        @Test
        @DisplayName("너무 긴 스팟 이름은 거부된다")
        fun createSpot_tooLongSpotName_fails() {
            // given
            val tooLongName = "a".repeat(101) // 100자 초과
            val createSpotRequest = CreateSpotRequest(
                spotName = tooLongName,
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "너무 긴 이름 테스트",
                spotLevel = 1,
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
                .statusCode(400)
        }

        @Test
        @DisplayName("너무 긴 스팟 정보는 거부된다")
        fun createSpot_tooLongSpotInfo_fails() {
            // given
            val tooLongInfo = "a".repeat(1001) // 1000자 초과
            val createSpotRequest = CreateSpotRequest(
                spotName = "테스트 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = tooLongInfo,
                spotLevel = 1,
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
                .statusCode(400)
        }

        @Test
        @DisplayName("잘못된 이미지 URL 형식은 거부된다")
        fun createSpot_invalidImageUrl_fails() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "이미지 URL 테스트",
                spotThumbnailImageLink = "invalid-url-format",
                spotAddress = "서울시 강남구",
                spotInfo = "잘못된 이미지 URL 테스트",
                spotLevel = 1,
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
                .statusCode(400)
        }

        @Test
        @DisplayName("악성 스크립트가 포함된 스팟 정보는 거부된다")
        fun createSpot_maliciousScript_fails() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "악성 스크립트 테스트",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "<script>alert('XSS')</script>악성 스크립트 테스트",
                spotLevel = 1,
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
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("스팟 경계값 테스트")
    inner class SpotBoundaryTest {

        @Test
        @DisplayName("최소 좌표값으로 스팟 생성이 성공한다")
        fun createSpot_minimumCoordinates_success() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "최소 좌표 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "최소 좌표 주소",
                spotInfo = "최소 좌표 테스트",
                spotLevel = 1,
                latitude = -90.0, // 최소 위도
                longitude = -180.0 // 최소 경도
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

        @Test
        @DisplayName("최대 좌표값으로 스팟 생성이 성공한다")
        fun createSpot_maximumCoordinates_success() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "최대 좌표 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "최대 좌표 주소",
                spotInfo = "최대 좌표 테스트",
                spotLevel = 5,
                latitude = 90.0, // 최대 위도
                longitude = 180.0 // 최대 경도
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

        @Test
        @DisplayName("최소 스팟 레벨로 생성이 성공한다")
        fun createSpot_minimumLevel_success() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "최소 레벨 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "최소 레벨 테스트",
                spotLevel = 1, // 최소 레벨
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

        @Test
        @DisplayName("최대 스팟 레벨로 생성이 성공한다")
        fun createSpot_maximumLevel_success() {
            // given
            val createSpotRequest = CreateSpotRequest(
                spotName = "최대 레벨 스팟",
                spotThumbnailImageLink = "https://example.com/image.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "최대 레벨 테스트",
                spotLevel = 5, // 최대 레벨 (추정)
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

    @Nested
    @DisplayName("스팟 중복성 테스트")
    inner class SpotDuplicationTest {

        @Test
        @DisplayName("동일한 이름의 스팟 생성이 가능하다")
        fun createSpot_duplicateName_success() {
            // given
            val spotName = "중복 테스트 스팟"
            val createSpotRequest1 = CreateSpotRequest(
                spotName = spotName,
                spotThumbnailImageLink = "https://example.com/image1.jpg",
                spotAddress = "서울시 강남구 위치1",
                spotInfo = "첫 번째 스팟",
                spotLevel = 1,
                latitude = 37.5665,
                longitude = 126.9780
            )

            val createSpotRequest2 = CreateSpotRequest(
                spotName = spotName,
                spotThumbnailImageLink = "https://example.com/image2.jpg",
                spotAddress = "서울시 강남구 위치2",
                spotInfo = "두 번째 스팟",
                spotLevel = 2,
                latitude = 37.5666,
                longitude = 126.9781
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest1)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(200)

            given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest2)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("동일한 좌표의 스팟 생성 시 실패한다")
        fun createSpot_duplicateCoordinates_fails() {
            // given
            val latitude = 37.5665
            val longitude = 126.9780

            val createSpotRequest1 = CreateSpotRequest(
                spotName = "첫 번째 스팟",
                spotThumbnailImageLink = "https://example.com/image1.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "첫 번째 스팟",
                spotLevel = 1,
                latitude = latitude,
                longitude = longitude
            )

            val createSpotRequest2 = CreateSpotRequest(
                spotName = "두 번째 스팟",
                spotThumbnailImageLink = "https://example.com/image2.jpg",
                spotAddress = "서울시 강남구",
                spotInfo = "두 번째 스팟 (동일 좌표)",
                spotLevel = 2,
                latitude = latitude,
                longitude = longitude
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest1)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(200)

            given()
                .contentType(ContentType.JSON)
                .body(createSpotRequest2)
            .`when`()
                .post("/api/spots")
            .then()
                .statusCode(409) // 또는 400 (중복 좌표)
        }
    }
}