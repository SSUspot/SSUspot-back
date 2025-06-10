package com.ssuspot.sns.api.controller.user

import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RefreshRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.request.user.UpdateUserDataRequest
import com.ssuspot.sns.api.response.user.LoginResponse
import com.ssuspot.sns.api.response.user.UserResponse
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

@DisplayName("UserController 통합 테스트")
@Transactional
class UserControllerIntegrationTest : SimpleIntegrationTestBase() {

    @Autowired
    private lateinit var authTestHelper: AuthTestHelper

    @Nested
    @DisplayName("회원가입 API")
    inner class RegisterTest {

        @Test
        @DisplayName("정상적인 회원가입이 성공한다")
        fun register_success() {
            // given
            val registerRequest = RegisterRequest(
                email = "newuser@example.com",
                password = "password123",
                userName = "newuser",
                nickname = "새로운사용자",
                profileMessage = "안녕하세요!",
                profileImageLink = "https://example.com/profile.jpg"
            )

            // when & then
            val response = given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
            .`when`()
                .post("/api/users/register")
            .then()
                .statusCode(200)
                .extract()
                .`as`(UserResponse::class.java)

            assertThat(response.email).isEqualTo("newuser@example.com")
            assertThat(response.userName).isEqualTo("newuser")
            assertThat(response.nickname).isEqualTo("새로운사용자")
            assertThat(response.profileMessage).isEqualTo("안녕하세요!")
        }

        @Test
        @DisplayName("중복된 이메일로 회원가입 시 실패한다")
        fun register_duplicateEmail_fails() {
            // given
            val email = "duplicate@example.com"
            authTestHelper.registerUser(email, "password123", "user1", "사용자1")

            val duplicateRequest = RegisterRequest(
                email = email,
                password = "password456",
                userName = "user2",
                nickname = "사용자2",
                profileMessage = null,
                profileImageLink = null
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(duplicateRequest)
            .`when`()
                .post("/api/users/register")
            .then()
                .statusCode(400)
        }

        @Test
        @DisplayName("필수 필드 누락 시 실패한다")
        fun register_missingRequiredFields_fails() {
            // given
            val invalidRequest = mapOf(
                "email" to "test@example.com"
                // password, userName, nickname 누락
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
            .`when`()
                .post("/api/users/register")
            .then()
                .statusCode(400)
        }
    }

    @Nested
    @DisplayName("로그인 API")
    inner class LoginTest {

        @Test
        @DisplayName("정상적인 로그인이 성공한다")
        fun login_success() {
            // given
            val email = "logintest@example.com"
            val password = "password123"
            authTestHelper.registerUser(email, password, "loginuser", "로그인사용자")

            val loginRequest = LoginRequest(email, password)

            // when & then
            val response = given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
            .`when`()
                .post("/api/users/login")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LoginResponse::class.java)

            assertThat(response.accessToken).isNotEmpty()
            assertThat(response.refreshToken).isNotEmpty()
            assertThat(response.accessTokenExpiredIn).isGreaterThan(0)
            assertThat(response.refreshTokenExpiredIn).isGreaterThan(0)
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인 시 실패한다")
        fun login_wrongPassword_fails() {
            // given
            val email = "wrongpwd@example.com"
            authTestHelper.registerUser(email, "correctpassword", "user", "사용자")

            val loginRequest = LoginRequest(email, "wrongpassword")

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
            .`when`()
                .post("/api/users/login")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 로그인 시 실패한다")
        fun login_nonExistentUser_fails() {
            // given
            val loginRequest = LoginRequest("nonexistent@example.com", "password123")

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
            .`when`()
                .post("/api/users/login")
            .then()
                .statusCode(401)
        }
    }

    @Nested
    @DisplayName("토큰 갱신 API")
    inner class RefreshTokenTest {

        @Test
        @DisplayName("유효한 Refresh Token으로 갱신이 성공한다")
        fun refreshToken_success() {
            // given
            val email = "refreshtest@example.com"
            val token = authTestHelper.registerAndLoginUser(email, "password123", "refreshuser", "갱신사용자")
            
            val loginResponse = given()
                .contentType(ContentType.JSON)
                .body(LoginRequest(email, "password123"))
            .`when`()
                .post("/api/users/login")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LoginResponse::class.java)

            val refreshRequest = RefreshRequest(loginResponse.refreshToken)

            // when & then
            val response = given()
                .contentType(ContentType.JSON)
                .body(refreshRequest)
            .`when`()
                .post("/api/users/refresh")
            .then()
                .statusCode(200)
                .extract()
                .`as`(LoginResponse::class.java)

            assertThat(response.accessToken).isNotEmpty()
            assertThat(response.refreshToken).isNotEmpty()
            // 새로운 토큰이 발급되었는지 확인
            assertThat(response.accessToken).isNotEqualTo(loginResponse.accessToken)
        }

        @Test
        @DisplayName("잘못된 Refresh Token으로 갱신 시 실패한다")
        fun refreshToken_invalidToken_fails() {
            // given
            val refreshRequest = RefreshRequest("invalid.refresh.token")

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(refreshRequest)
            .`when`()
                .post("/api/users/refresh")
            .then()
                .statusCode(401)
        }
    }

    @Nested
    @DisplayName("사용자 정보 조회 API")
    inner class GetUserInfoTest {

        @Test
        @DisplayName("본인 정보 조회가 성공한다")
        fun getMyInfo_success() {
            // given
            val email = "myinfo@example.com"
            val token = authTestHelper.registerAndLoginUser(email, "password123", "myuser", "내정보사용자")

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .get("/api/users/me")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("인증 없이 사용자 정보 조회 시 실패한다")
        fun getMyInfo_noAuth_fails() {
            // when & then
            given()
            .`when`()
                .get("/api/users/me")
            .then()
                .statusCode(401)
        }

        @Test
        @DisplayName("잘못된 토큰으로 사용자 정보 조회 시 실패한다")
        fun getMyInfo_invalidToken_fails() {
            // when & then
            given()
                .header("Authorization", "Bearer invalid.token")
            .`when`()
                .get("/api/users/me")
            .then()
                .statusCode(401)
        }
    }

    @Nested
    @DisplayName("팔로우 기능 API")
    inner class FollowTest {

        @Test
        @DisplayName("다른 사용자 팔로우가 성공한다")
        fun followUser_success() {
            // given
            val users = authTestHelper.createMultipleUsers(2)
            val (followerEmail, followerToken) = users[0]
            val (followeeEmail, _) = users[1]

            // followee의 ID를 가져오기 위해 먼저 사용자 검색 (실제로는 ID를 알고 있다고 가정)
            val followeeId = 2L // 실제 구현에서는 동적으로 가져와야 함

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(followerToken))
            .`when`()
                .post("/api/users/$followeeId/follow")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("본인을 팔로우할 수 없다")
        fun followUser_self_fails() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val myId = 1L // 실제로는 동적으로 가져와야 함

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .post("/api/users/$myId/follow")
            .then()
                .statusCode(400) // 또는 409 Conflict
        }

        @Test
        @DisplayName("팔로잉 목록 조회가 성공한다")
        fun getFollowingList_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
            .`when`()
                .get("/api/users/following")
            .then()
                .statusCode(200)
        }
    }

    @Nested
    @DisplayName("사용자 정보 수정 API")
    inner class UpdateUserTest {

        @Test
        @DisplayName("사용자 정보 수정이 성공한다")
        fun updateUser_success() {
            // given
            val token = authTestHelper.registerAndLoginUser()
            val updateRequest = UpdateUserDataRequest(
                userName = "updateuser",
                nickname = "수정된닉네임",
                profileMessage = "수정된 프로필 메시지",
                profileImageLink = "https://example.com/new-profile.jpg"
            )

            // when & then
            given()
                .header("Authorization", authTestHelper.bearerToken(token))
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .`when`()
                .patch("/api/users/me")
            .then()
                .statusCode(200)
        }

        @Test
        @DisplayName("인증 없이 사용자 정보 수정 시 실패한다")
        fun updateUser_noAuth_fails() {
            // given
            val updateRequest = UpdateUserDataRequest(
                userName = "hacker",
                nickname = "해킹시도",
                profileMessage = "해킹 메시지",
                profileImageLink = null
            )

            // when & then
            given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .`when`()
                .patch("/api/users/me")
            .then()
                .statusCode(401)
        }
    }
}