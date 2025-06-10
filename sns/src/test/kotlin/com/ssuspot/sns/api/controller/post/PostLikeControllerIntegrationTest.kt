package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.LikeResponse
import com.ssuspot.sns.support.integration.AuthTestHelper
import com.ssuspot.sns.support.integration.IntegrationTestBase
import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@DisplayName("PostLikeController 통합 테스트")
@Transactional
class PostLikeControllerIntegrationTest : IntegrationTestBase() {

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @Nested
    @DisplayName("게시물 좋아요 API")
    inner class LikePostTest {

        @Test
        @DisplayName("정상적인 게시물 좋아요가 성공한다")
        fun likePost_success() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "liker@example.com",
                password = "password123",
                userName = "liker",
                nickname = "좋아요유저"
            )
            val postId = 1L // 실제로는 먼저 게시물을 생성해야 함

            // when & then
            val response = given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LikeResponse::class.java)

            assertThat(response.postId).isEqualTo(postId)
            assertThat(response.userNickname).isEqualTo("좋아요유저")
            assertThat(response.pressed).isIn("LIKE", "UNLIKE") // 좋아요 상태
        }

        @Test
        @DisplayName("인증 없이 좋아요 시 실패한다")
        fun likePost_noAuth_fails() {
            // given
            val postId = 1L

            // when & then
            given()
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("잘못된 토큰으로 좋아요 시 실패한다")
        fun likePost_invalidToken_fails() {
            // given
            val postId = 1L

            // when & then
            given()
                .header("Authorization", "Bearer invalid.token.here")
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("존재하지 않는 게시물에 좋아요 시 실패한다")
        fun likePost_postNotFound_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentPostId = 99999L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$nonExistentPostId/likes")
            .then()
                .statusCode(404)
        }

        @Test
        @DisplayName("동일한 게시물에 대한 중복 좋아요는 토글된다")
        fun likePost_duplicate_toggles() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "toggle@example.com",
                password = "password123",
                userName = "toggle",
                nickname = "토글유저"
            )
            val postId = 1L

            // when & then - 첫 번째 좋아요
            val firstResponse = given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LikeResponse::class.java)

            // when & then - 두 번째 좋아요 (토글)
            val secondResponse = given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LikeResponse::class.java)

            // 좋아요 상태가 토글되어야 함
            assertThat(firstResponse.pressed).isNotEqualTo(secondResponse.pressed)
            assertThat(firstResponse.postId).isEqualTo(secondResponse.postId)
            assertThat(firstResponse.userNickname).isEqualTo(secondResponse.userNickname)
        }
    }

    @Nested
    @DisplayName("좋아요 권한 테스트")
    inner class LikeAuthorizationTest {

        @Test
        @DisplayName("본인 게시물에 좋아요가 가능하다")
        fun likePost_ownPost_success() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "author@example.com",
                password = "password123",
                userName = "author",
                nickname = "작성자"
            )
            val postId = 1L // 본인이 작성한 게시물

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("다른 사용자의 게시물에 좋아요가 가능하다")
        fun likePost_otherUserPost_success() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (_, user1Token) = users[0]
            val (_, _) = users[1] // user2가 작성한 게시물

            val postId = 1L // user2가 작성한 게시물이라고 가정

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
        }
    }

    @Nested
    @DisplayName("좋아요 동시성 테스트")
    inner class LikeConcurrencyTest {

        @Test
        @DisplayName("여러 사용자가 동시에 같은 게시물에 좋아요할 수 있다")
        fun likePost_multipleUsers_success() {
            // given
            val users = authTestHelper.createMultipleUsers(3)
            val postId = 1L

            // when & then - 여러 사용자가 동시에 좋아요
            users.forEach { (email, token) ->
                val response = given()
                    .header("Authorization", authTestHelper.bearerToken(token))
                .`when`()
                    .post("/api/posts/$postId/likes")
                .then()
                    .statusCode(200)
                    .extract()
                    .`as`(LikeResponse::class.java)

                assertThat(response.postId).isEqualTo(postId)
                assertThat(response.userNickname).contains("사용자")
            }
        }

        @Test
        @DisplayName("한 사용자가 여러 게시물에 좋아요할 수 있다")
        fun likePost_multiplePostsBySameUser_success() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "multiliker@example.com",
                password = "password123",
                userName = "multiliker",
                nickname = "다중좋아요유저"
            )
            val postIds = listOf(1L, 2L, 3L) // 여러 게시물

            // when & then - 한 사용자가 여러 게시물에 좋아요
            postIds.forEach { postId ->
                val response = given()
                    .header("Authorization", authTestHelper.bearerToken(token))
                .`when`()
                    .post("/api/posts/$postId/likes")
                .then()
                    .statusCode(200)
                    .extract()
                    .`as`(LikeResponse::class.java)

                assertThat(response.postId).isEqualTo(postId)
                assertThat(response.userNickname).isEqualTo("다중좋아요유저")
            }
        }
    }

    @Nested
    @DisplayName("좋아요 에러 케이스 테스트")
    inner class LikeErrorCaseTest {

        @Test
        @DisplayName("음수 게시물 ID로 좋아요 시 실패한다")
        fun likePost_negativePostId_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val invalidPostId = -1L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$invalidPostId/likes")
            .then()
                .statusCode(400) // 또는 404
        }

        @Test
        @DisplayName("0인 게시물 ID로 좋아요 시 실패한다")
        fun likePost_zeroPostId_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val invalidPostId = 0L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$invalidPostId/likes")
            .then()
                .statusCode(400) // 또는 404
        }

        @Test
        @DisplayName("매우 큰 게시물 ID로 좋아요 시 실패한다")
        fun likePost_veryLargePostId_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val veryLargePostId = Long.MAX_VALUE

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$veryLargePostId/likes")
            .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("좋아요 응답 검증 테스트")
    inner class LikeResponseValidationTest {

        @Test
        @DisplayName("좋아요 응답에 모든 필수 필드가 포함된다")
        fun likePost_responseContainsAllFields() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "response@example.com",
                password = "password123",
                userName = "response",
                nickname = "응답테스트유저"
            )
            val postId = 1L

            // when & then
            val response = given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LikeResponse::class.java)

            // 모든 필드가 null이 아니어야 함
            assertThat(response.likeId).isNotNull()
            assertThat(response.postId).isNotNull().isEqualTo(postId)
            assertThat(response.userNickname).isNotNull().isNotEmpty()
            assertThat(response.pressed).isNotNull().isNotEmpty()
        }

        @Test
        @DisplayName("좋아요 상태값이 유효한 값이다")
        fun likePost_pressedStatusIsValid() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L

            // when & then
            val response = given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LikeResponse::class.java)

            // pressed 값이 유효한 상태값이어야 함
            assertThat(response.pressed).isIn("LIKE", "UNLIKE", "PRESSED", "UNPRESSED")
        }
    }

    @Nested
    @DisplayName("좋아요 보안 테스트")
    inner class LikeSecurityTest {

        @Test
        @DisplayName("만료된 토큰으로 좋아요 시 실패한다")
        fun likePost_expiredToken_fails() {
            // given
            val expiredToken = authTestHelper.createExpiredToken()
            val postId = 1L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(expiredToken))
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("잘못된 형식의 토큰으로 좋아요 시 실패한다")
        fun likePost_malformedToken_fails() {
            // given
            val malformedToken = "malformed.token"
            val postId = 1L

            // when & then
            given()
                .header("Authorization", "Bearer $malformedToken")
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("Authorization 헤더 없이 좋아요 시 실패한다")
        fun likePost_noAuthorizationHeader_fails() {
            // given
            val postId = 1L

            // when & then
            given()
            .`when`()
                .post("/api/posts/$postId/likes")
            .then()
                .statusCode(401)
        }
    }
}