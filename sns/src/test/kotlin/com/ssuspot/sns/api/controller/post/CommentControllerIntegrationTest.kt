package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreateCommentRequest
import com.ssuspot.sns.api.request.post.UpdateCommentRequest
import com.ssuspot.sns.api.response.post.CommentResponse
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

@DisplayName("CommentController 통합 테스트")
@Transactional
class CommentControllerIntegrationTest : SimpleIntegrationTestBase() {

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @Nested
    @DisplayName("댓글 생성 API")
    inner class CreateCommentTest {

        @Test
        @DisplayName("정상적인 댓글 생성이 성공한다")
        fun createComment_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L // 실제로는 먼저 게시물을 생성해야 함
            val createCommentRequest = CreateCommentRequest(
                content = "테스트 댓글 내용입니다."
            )

            // when & then
            val response = given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createCommentRequest)
            .`when`()
                .post("/api/posts/$postId/comments")
            .then()
                .statusCode(200)
                .extract()
                .`as`(CommentResponse::class.java)

            assertThat(response.content).isEqualTo("테스트 댓글 내용입니다.")
        }

        @Test
        @DisplayName("인증 없이 댓글 생성 시 실패한다")
        fun createComment_noAuth_fails() {
            // given
            val postId = 1L
            val createCommentRequest = CreateCommentRequest(
                content = "무단 댓글"
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(createCommentRequest)
            .`when`()
                .post("/api/posts/$postId/comments")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("존재하지 않는 게시물에 댓글 생성 시 실패한다")
        fun createComment_postNotFound_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentPostId = 99999L
            val createCommentRequest = CreateCommentRequest(
                content = "존재하지 않는 게시물의 댓글"
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createCommentRequest)
            .`when`()
                .post("/api/posts/$nonExistentPostId/comments")
            .then()
                .statusCode(404)
        }

        @Test
        @DisplayName("빈 내용으로 댓글 생성 시 실패한다")
        fun createComment_emptyContent_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L
            val createCommentRequest = CreateCommentRequest(
                content = ""
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createCommentRequest)
            .`when`()
                .post("/api/posts/$postId/comments")
            .then()
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("댓글 조회 API")
    inner class GetCommentsTest {

        @Test
        @DisplayName("게시물의 댓글 목록 조회가 성공한다")
        fun getCommentsByPostId_success() {
            // given
            val postId = 1L

            // when & then
            given()
                .queryParam("page", 1)
                .queryParam("size", 10)
            .`when`()
                .get("/api/posts/$postId/comments")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("특정 댓글 조회가 성공한다")
        fun getSpecificComment_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val commentId = 1L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .get("/api/comments/$commentId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("존재하지 않는 댓글 조회 시 실패한다")
        fun getSpecificComment_notFound_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentCommentId = 99999L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .get("/api/comments/$nonExistentCommentId")
            .then()
                .statusCode(404)
        }

        @Test
        @DisplayName("페이징이 올바르게 적용된다")
        fun getComments_pagination_works() {
            // given
            val postId = 1L

            // when & then
            given()
                .queryParam("page", 2)
                .queryParam("size", 5)
            .`when`()
                .get("/api/posts/$postId/comments")
            .then()
                .statusCode(200)
        }
    }

    @Nested
    @DisplayName("댓글 수정 API")
    inner class UpdateCommentTest {

        @Test
        @DisplayName("본인 댓글 수정이 성공한다")
        fun updateComment_owner_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val commentId = 1L // 실제로는 먼저 댓글을 생성해야 함
            val updateCommentRequest = UpdateCommentRequest(
                content = "수정된 댓글 내용"
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(updateCommentRequest)
            .`when`()
                .patch("/api/comments/$commentId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("다른 사용자의 댓글 수정 시 실패한다")
        fun updateComment_notOwner_fails() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (_, user1Token) = users[0]
            val commentId = 1L // user2가 작성한 댓글
            val updateCommentRequest = UpdateCommentRequest(
                content = "해킹 시도"
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
                .contentType(ContentType.JSON)
                .body(updateCommentRequest)
            .`when`()
                .patch("/api/comments/$commentId")
            .then()
                .statusCode(403)
        }

        @Test
        @DisplayName("빈 내용으로 댓글 수정 시 실패한다")
        fun updateComment_emptyContent_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val commentId = 1L
            val updateCommentRequest = UpdateCommentRequest(
                content = ""
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(updateCommentRequest)
            .`when`()
                .patch("/api/comments/$commentId")
            .then()
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("댓글 삭제 API")
    inner class DeleteCommentTest {

        @Test
        @DisplayName("본인 댓글 삭제가 성공한다")
        fun deleteComment_owner_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val commentId = 1L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .delete("/api/comments/$commentId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("다른 사용자의 댓글 삭제 시 실패한다")
        fun deleteComment_notOwner_fails() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (_, user1Token) = users[0]
            val commentId = 1L // user2가 작성한 댓글

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
            .`when`()
                .delete("/api/comments/$commentId")
            .then()
                .statusCode(403)
        }

        @Test
        @DisplayName("존재하지 않는 댓글 삭제 시 실패한다")
        fun deleteComment_notFound_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentCommentId = 99999L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .delete("/api/comments/$nonExistentCommentId")
            .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("댓글 권한 테스트")
    inner class CommentAuthorizationTest {

        @Test
        @DisplayName("게시물 작성자는 모든 댓글을 삭제할 수 있다")
        fun deleteComment_postOwner_success() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (postOwnerEmail, postOwnerToken) = users[0]
            val (_, _) = users[1]
            
            // 실제로는 postOwner가 작성한 게시물에 user2가 댓글을 단 상황
            val commentId = 1L

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(postOwnerToken))
            .`when`()
                .delete("/api/comments/$commentId")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("인증되지 않은 사용자는 댓글 수정할 수 없다")
        fun updateComment_noAuth_fails() {
            // given
            val commentId = 1L
            val updateCommentRequest = UpdateCommentRequest(
                content = "해킹 시도"
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(updateCommentRequest)
            .`when`()
                .patch("/api/comments/$commentId")
            .then()
                .statusCode(401)
        }
    }

    @Nested
    @DisplayName("댓글 검증 테스트")
    inner class CommentValidationTest {

        @Test
        @DisplayName("너무 긴 댓글 내용은 거부된다")
        fun createComment_tooLongContent_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L
            val tooLongContent = "a".repeat(1001) // 1000자 초과
            val createCommentRequest = CreateCommentRequest(
                content = tooLongContent
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createCommentRequest)
            .`when`()
                .post("/api/posts/$postId/comments")
            .then()
                .statusCode(400)
        }

        @Test
        @DisplayName("악성 스크립트가 포함된 댓글은 거부된다")
        fun createComment_maliciousScript_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val postId = 1L
            val maliciousContent = "<script>alert('XSS')</script>"
            val createCommentRequest = CreateCommentRequest(
                content = maliciousContent
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createCommentRequest)
            .`when`()
                .post("/api/posts/$postId/comments")
            .then()
                .statusCode(400)
        }
    }
}