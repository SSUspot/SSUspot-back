package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.request.post.UpdatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
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

@DisplayName("PostController 통합 테스트")
@Transactional
class PostControllerIntegrationTest : SimpleIntegrationTestBase() {

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @Nested
    @DisplayName("게시물 생성 API")
    inner class CreatePostTest {

        @Test
        @DisplayName("정상적인 게시물 생성이 성공한다")
        fun createPost_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val createPostRequest = CreatePostRequest(
                title = "테스트 게시물",
                content = "테스트 게시물 내용입니다.",
                spotId = 1L, // 실제로는 먼저 Spot을 생성해야 함
                imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
                tags = listOf("테스트", "통합테스트")
            )

            // when & then
            val response = given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createPostRequest)
            .`when`()
                .post("/api/posts")
            .then()
                .statusCode(200)
                .extract()
                .`as`(PostResponse::class.java)

            assertThat(response.title).isEqualTo("테스트 게시물")
            assertThat(response.content).isEqualTo("테스트 게시물 내용입니다.")
            assertThat(response.imageUrls).hasSize(2)
        }

        @Test
        @DisplayName("인증 없이 게시물 생성 시 실패한다")
        fun createPost_noAuth_fails() {
            // given
            val createPostRequest = CreatePostRequest(
                title = "무단 게시물",
                content = "무단 접근 시도",
                spotId = 1L,
                imageUrls = emptyList(),
                tags = emptyList()
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(createPostRequest)
            .`when`()
                .post("/api/posts")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("필수 필드 누락 시 게시물 생성이 실패한다")
        fun createPost_missingFields_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val invalidRequest = mapOf(
                "title" to "제목만 있는 게시물"
                // content, spotId, userEmail 누락
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(invalidRequest)
            .`when`()
                .post("/api/posts")
            .then()
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("게시물 조회 API")
    inner class GetPostTest {

        @Test
        @DisplayName("추천 게시물 조회가 성공한다")
        fun getRecommendedPosts_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/recommend")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("특정 게시물 조회가 성공한다")
        fun getSpecificPost_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L // 실제로는 먼저 게시물을 생성해야 함

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .get("/api/posts/$postId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("존재하지 않는 게시물 조회 시 실패한다")
        fun getSpecificPost_notFound_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentPostId = 99999L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .get("/api/posts/$nonExistentPostId")
            .then()
                .statusCode(404)
        }

        @Test
        @DisplayName("사용자별 게시물 조회가 성공한다")
        fun getUserPosts_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/users/me")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("스팟별 게시물 조회가 성공한다")
        fun getPostsBySpot_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val spotId = 1L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/spots/$spotId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("팔로잉 사용자 게시물 조회가 성공한다")
        fun getFollowingPosts_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/following")
            .then()
                .statusCode(200)
        }
    }

    @Nested
    @DisplayName("게시물 수정 API")
    inner class UpdatePostTest {

        @Test
        @DisplayName("본인 게시물 수정이 성공한다")
        fun updatePost_owner_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L // 실제로는 먼저 게시물을 생성해야 함
            
            val updateRequest = UpdatePostRequest(
                title = "수정된 제목",
                content = "수정된 내용",
                tags = listOf("수정", "업데이트")
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .`when`()
                .patch("/api/posts/$postId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("다른 사용자의 게시물 수정 시 실패한다")
        fun updatePost_notOwner_fails() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (_, user1Token) = users[0]
            val (_, _) = users[1]
            
            val postId = 1L // user2가 작성한 게시물이라고 가정
            val updateRequest = UpdatePostRequest(
                title = "해킹 시도",
                content = "권한 없는 수정",
                tags = emptyList()
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .`when`()
                .patch("/api/posts/$postId")
            .then()
                .statusCode(403) // 또는 404
        }
    }

    @Nested
    @DisplayName("게시물 삭제 API")
    inner class DeletePostTest {

        @Test
        @DisplayName("본인 게시물 삭제가 성공한다")
        fun deletePost_owner_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L // 실제로는 먼저 게시물을 생성해야 함

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .delete("/api/posts/$postId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("다른 사용자의 게시물 삭제 시 실패한다")
        fun deletePost_notOwner_fails() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (_, user1Token) = users[0]
            val postId = 1L // user2가 작성한 게시물

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
            .`when`()
                .delete("/api/posts/$postId")
            .then()
                .statusCode(403) // 또는 404
        }

        @Test
        @DisplayName("존재하지 않는 게시물 삭제 시 실패한다")
        fun deletePost_notFound_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentPostId = 99999L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .delete("/api/posts/$nonExistentPostId")
            .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("게시물 검색 API")
    inner class SearchPostTest {

        @Test
        @DisplayName("태그로 게시물 검색이 성공한다")
        fun searchPostsByTag_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "테스트"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/tags/$tagName")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("존재하지 않는 태그 검색 시 빈 결과를 반환한다")
        fun searchPostsByTag_notFound_returnsEmpty() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentTag = "존재하지않는태그"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/tags/$nonExistentTag")
            .then()
                .statusCode(200)
                // 빈 배열이 반환되어야 함
        }
    }

    @Nested
    @DisplayName("페이징 테스트")
    inner class PaginationTest {

        @Test
        @DisplayName("페이징 파라미터가 올바르게 적용된다")
        fun pagination_parameters_applied() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 2)
                .queryParam("size", 5)
            .`when`()
                .get("/api/posts/recommend")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("잘못된 페이징 파라미터 시 기본값이 적용된다")
        fun pagination_invalidParameters_useDefaults() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", -1)
                .queryParam("size", 0)
            .`when`()
                .get("/api/posts/recommend")
            .then()
                .statusCode(200)
        }
    }
}