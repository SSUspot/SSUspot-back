package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.support.integration.AuthTestHelper
import com.ssuspot.sns.support.integration.IntegrationTestBase
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@DisplayName("Tag 기능 통합 테스트")
@Transactional
class TagIntegrationTest : IntegrationTestBase() {

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @Nested
    @DisplayName("태그로 게시물 검색 API")
    inner class SearchPostsByTagTest {

        @Test
        @DisplayName("정상적인 태그 검색이 성공한다")
        fun searchPostsByTag_success() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "tagger@example.com",
                password = "password123",
                userName = "tagger",
                nickname = "태그유저"
            )
            val tagName = "여행"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("인증 없이 태그 검색 시 실패한다")
        fun searchPostsByTag_noAuth_fails() {
            // given
            val tagName = "여행"

            // when & then
            given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("태그 이름이 없으면 실패한다")
        fun searchPostsByTag_noTagName_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                // tagName 누락
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(400)
        }

        @Test
        @DisplayName("존재하지 않는 태그 검색 시 빈 배열을 반환한다")
        fun searchPostsByTag_nonExistentTag_returnsEmpty() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val nonExistentTag = "존재하지않는태그12345"

            // when & then
            val response = given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", nonExistentTag)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", PostResponse::class.java)

            assertThat(response).isEmpty()
        }

        @Test
        @DisplayName("빈 태그 이름으로 검색 시 실패한다")
        fun searchPostsByTag_emptyTagName_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", "")
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("태그 검색 페이징 테스트")
    inner class TagSearchPaginationTest {

        @Test
        @DisplayName("페이징 파라미터가 올바르게 적용된다")
        fun searchPostsByTag_pagination_works() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "테스트태그"

            // when & then - 첫 번째 페이지
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 5)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)

            // when & then - 두 번째 페이지
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 2)
                .queryParam("size", 5)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("잘못된 페이징 파라미터 시 기본값이 적용된다")
        fun searchPostsByTag_invalidPagination_useDefaults() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "테스트태그"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", -1)
                .queryParam("size", 0)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("큰 페이지 크기로 검색해도 성공한다")
        fun searchPostsByTag_largePageSize_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "테스트태그"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 100)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }
    }

    @Nested
    @DisplayName("태그 검증 테스트")
    inner class TagValidationTest {

        @Test
        @DisplayName("특수문자가 포함된 태그 검색이 가능하다")
        fun searchPostsByTag_specialCharacters_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "C++"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("숫자가 포함된 태그 검색이 가능하다")
        fun searchPostsByTag_withNumbers_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "React18"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("한글 태그 검색이 가능하다")
        fun searchPostsByTag_korean_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "맛집"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("영문 태그 검색이 가능하다")
        fun searchPostsByTag_english_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tagName = "technology"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("너무 긴 태그 이름으로 검색 시 실패한다")
        fun searchPostsByTag_tooLongTagName_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val tooLongTagName = "a".repeat(101) // 100자 초과

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tooLongTagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("태그 기반 게시물 생성 및 검색 통합 테스트")
    inner class TagPostIntegrationTest {

        @Test
        @DisplayName("태그가 포함된 게시물 생성 후 태그로 검색이 가능하다")
        fun createPostWithTags_thenSearchByTag_success() {
            // given
            val token = authTestHelper.registerAndLoginUser(
                email = "tagpost@example.com",
                password = "password123",
                userName = "tagpost",
                nickname = "태그게시물유저"
            )
            
            val tags = listOf("여행", "맛집", "서울")
            val createPostRequest = CreatePostRequest(
                title = "서울 맛집 여행기",
                content = "서울의 맛있는 맛집들을 소개합니다!",
                spotId = 1L,
                userEmail = "tagpost@example.com",
                imageUrls = listOf("https://example.com/food1.jpg"),
                tags = tags
            )

            // 게시물 생성
            val createdPost = given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(createPostRequest)
            .`when`()
                .post("/api/posts")
            .then()
                .statusCode(200)
                .extract()
                .`as`(PostResponse::class.java)

            // when & then - 각 태그로 검색하여 생성된 게시물이 검색되는지 확인
            tags.forEach { tag ->
                val searchResults = given()
                    .header("Authorization", authTestHelper.bearerToken(token))
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .queryParam("tagName", tag)
                .`when`()
                    .get("/api/tags")
                .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList(".", PostResponse::class.java)

                // 생성된 게시물이 검색 결과에 포함되어야 함
                assertThat(searchResults).anySatisfy { post ->
                    assertThat(post.title).isEqualTo("서울 맛집 여행기")
                }
            }
        }

        @Test
        @DisplayName("여러 게시물에 같은 태그가 있을 때 모두 검색된다")
        fun multiplePostsWithSameTag_allReturned() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (_, user1Token) = users[0]
            val (_, user2Token) = users[1]
            
            val commonTag = "공통태그"

            // user1이 게시물 작성
            val post1Request = CreatePostRequest(
                title = "첫 번째 게시물",
                content = "공통 태그가 포함된 첫 번째 게시물",
                spotId = 1L,
                userEmail = "user1@example.com",
                imageUrls = emptyList(),
                tags = listOf(commonTag, "태그1")
            )

            given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
                .contentType(ContentType.JSON)
                .body(post1Request)
            .`when`()
                .post("/api/posts")
            .then()
                .statusCode(200)

            // user2가 게시물 작성
            val post2Request = CreatePostRequest(
                title = "두 번째 게시물",
                content = "공통 태그가 포함된 두 번째 게시물",
                spotId = 1L,
                userEmail = "user2@example.com",
                imageUrls = emptyList(),
                tags = listOf(commonTag, "태그2")
            )

            given()
                .header("Authorization", authTestHelper.bearerToken(user2Token))
                .contentType(ContentType.JSON)
                .body(post2Request)
            .`when`()
                .post("/api/posts")
            .then()
                .statusCode(200)

            // when & then - 공통 태그로 검색
            val searchResults = given()
                .header("Authorization", authTestHelper.bearerToken(user1Token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", commonTag)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", PostResponse::class.java)

            // 두 게시물 모두 검색되어야 함
            assertThat(searchResults).hasSize(2)
            val titles = searchResults.map { it.title }
            assertThat(titles).containsExactlyInAnyOrder("첫 번째 게시물", "두 번째 게시물")
        }
    }

    @Nested
    @DisplayName("태그 검색 보안 테스트")
    inner class TagSearchSecurityTest {

        @Test
        @DisplayName("만료된 토큰으로 태그 검색 시 실패한다")
        fun searchPostsByTag_expiredToken_fails() {
            // given
            val expiredToken = authTestHelper.createExpiredToken()
            val tagName = "테스트태그"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(expiredToken))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", tagName)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("악성 스크립트가 포함된 태그 검색을 막는다")
        fun searchPostsByTag_maliciousScript_blocked() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val maliciousTag = "<script>alert('XSS')</script>"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", maliciousTag)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(400) // 또는 필터링되어 빈 결과 반환
        }

        @Test
        @DisplayName("SQL 인젝션 시도를 막는다")
        fun searchPostsByTag_sqlInjection_blocked() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val sqlInjectionTag = "'; DROP TABLE posts; --"

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("tagName", sqlInjectionTag)
            .`when`()
                .get("/api/tags")
            .then()
                .statusCode(400) // 또는 안전하게 처리되어 빈 결과 반환
        }
    }

    @Nested
    @DisplayName("태그 대소문자 구분 테스트")
    inner class TagCaseSensitivityTest {

        @Test
        @DisplayName("태그 검색은 대소문자를 구분하지 않는다")
        fun searchPostsByTag_caseInsensitive() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val originalTag = "JavaScript"
            val lowerCaseTag = "javascript"
            val upperCaseTag = "JAVASCRIPT"

            // when & then - 다양한 대소문자 조합으로 검색
            val results = listOf(originalTag, lowerCaseTag, upperCaseTag).map { tag ->
                given()
                    .header("Authorization", authTestHelper.bearerToken(token))
                    .queryParam("page", 1)
                    .queryParam("size", 10)
                    .queryParam("tagName", tag)
                .`when`()
                    .get("/api/tags")
                .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList(".", PostResponse::class.java)
            }

            // 모든 검색 결과가 동일해야 함 (대소문자 구분하지 않음)
            val firstResult = results[0]
            results.forEach { result ->
                assertThat(result).isEqualTo(firstResult)
            }
        }
    }
}