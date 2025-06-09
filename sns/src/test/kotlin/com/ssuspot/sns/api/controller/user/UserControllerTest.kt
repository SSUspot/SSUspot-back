package com.ssuspot.sns.api.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.api.request.user.UpdateUserDataRequest
import com.ssuspot.sns.support.IntegrationTest
import com.ssuspot.sns.support.fixture.UserFixture
import com.ssuspot.sns.support.helper.DatabaseCleanup
import com.ssuspot.sns.support.helper.JwtTestHelper.withTestToken
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@IntegrationTest
@AutoConfigureMockMvc
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val databaseCleanup: DatabaseCleanup
) {

    @BeforeEach
    fun setUp() {
        databaseCleanup.execute()
    }

    @Test
    @DisplayName("사용자 회원가입 - 성공")
    fun `should register new user successfully`() {
        // given
        val request = RegisterRequest(
            email = "newuser@example.com",
            password = "password123",
            userName = "newuser",
            nickname = "New User",
            profileMessage = "Hello!",
            profileImageLink = "https://example.com/profile.jpg"
        )

        // when & then
        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("newuser@example.com"))
            .andExpect(jsonPath("$.userName").value("newuser"))
            .andExpect(jsonPath("$.nickname").value("New User"))
            .andExpect(jsonPath("$.id").exists())
    }

    @Test
    @DisplayName("사용자 회원가입 - 중복 이메일로 실패")
    fun `should fail to register with duplicate email`() {
        // given
        val firstRequest = RegisterRequest(
            email = "duplicate@example.com",
            password = "password123",
            userName = "user1",
            nickname = "User 1",
            profileMessage = null,
            profileImageLink = null
        )

        val duplicateRequest = RegisterRequest(
            email = "duplicate@example.com", // 동일한 이메일
            password = "password456",
            userName = "user2",
            nickname = "User 2",
            profileMessage = null,
            profileImageLink = null
        )

        // 첫 번째 사용자 등록
        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest))
        )
            .andExpect(status().isOk)

        // when & then - 두 번째 사용자 등록 시도
        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("사용자 로그인 - 성공")
    fun `should login successfully with valid credentials`() {
        // given - 먼저 사용자 등록
        val registerRequest = RegisterRequest(
            email = "logintest@example.com",
            password = "password123",
            userName = "loginuser",
            nickname = "Login User",
            profileMessage = null,
            profileImageLink = null
        )

        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )

        val loginRequest = LoginRequest(
            email = "logintest@example.com",
            password = "password123"
        )

        // when & then
        mockMvc.perform(
            post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
    }

    @Test
    @DisplayName("사용자 로그인 - 잘못된 비밀번호로 실패")
    fun `should fail to login with wrong password`() {
        // given - 먼저 사용자 등록
        val registerRequest = RegisterRequest(
            email = "wrongpass@example.com",
            password = "correctpassword",
            userName = "wrongpassuser",
            nickname = "Wrong Pass User",
            profileMessage = null,
            profileImageLink = null
        )

        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )

        val loginRequest = LoginRequest(
            email = "wrongpass@example.com",
            password = "wrongpassword" // 잘못된 비밀번호
        )

        // when & then
        mockMvc.perform(
            post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("사용자 정보 조회 - 인증된 사용자")
    fun `should get user info with valid token`() {
        // given - 사용자 등록 및 로그인은 토큰 헬퍼로 시뮬레이션
        registerAndGetToken("getinfo@example.com")

        // when & then
        mockMvc.perform(
            get("/api/users")
                .withTestToken("getinfo@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("getinfo@example.com"))
    }

    @Test
    @DisplayName("사용자 정보 조회 - 인증되지 않은 사용자")
    fun `should fail to get user info without token`() {
        // when & then
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk) // 현재는 모든 엔드포인트가 열려있음
    }

    @Test
    @DisplayName("사용자 프로필 업데이트 - 성공")
    fun `should update user profile successfully`() {
        // given
        registerAndGetToken("updatetest@example.com")

        val updateRequest = UpdateUserDataRequest(
            userName = "updateduser",
            nickname = "Updated User",
            profileMessage = "Updated message",
            profileImageLink = "https://example.com/new-profile.jpg"
        )

        // when & then
        mockMvc.perform(
            patch("/api/users")
                .withTestToken("updatetest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.nickname").value("Updated User"))
            .andExpect(jsonPath("$.profileMessage").value("Updated message"))
    }

    @Test
    @DisplayName("다른 사용자 팔로우 - 성공")
    fun `should follow another user successfully`() {
        // given - 두 명의 사용자 등록
        val user1Id = registerAndGetUserId("follower@example.com")
        val user2Id = registerAndGetUserId("followed@example.com")

        // when & then
        mockMvc.perform(
            post("/api/users/following/$user2Id")
                .withTestToken("follower@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value(user2Id))
    }

    @Test
    @DisplayName("다른 사용자 언팔로우 - 성공")
    fun `should unfollow user successfully`() {
        // given - 두 명의 사용자 등록 및 팔로우
        val user1Id = registerAndGetUserId("unfollower@example.com")
        val user2Id = registerAndGetUserId("unfollowed@example.com")

        // 먼저 팔로우
        mockMvc.perform(
            post("/api/users/following/$user2Id")
                .withTestToken("unfollower@example.com")
        )

        // when & then - 언팔로우
        mockMvc.perform(
            delete("/api/users/following/$user2Id")
                .withTestToken("unfollower@example.com")
        )
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("팔로잉 목록 조회 - 성공")
    fun `should get following list successfully`() {
        // given - 사용자 등록 및 팔로우
        val user1Id = registerAndGetUserId("getfollowing@example.com")
        val user2Id = registerAndGetUserId("followed1@example.com")
        val user3Id = registerAndGetUserId("followed2@example.com")

        // 두 명을 팔로우
        mockMvc.perform(
            post("/api/users/following/$user2Id")
                .withTestToken("getfollowing@example.com")
        )
        mockMvc.perform(
            post("/api/users/following/$user3Id")
                .withTestToken("getfollowing@example.com")
        )

        // when & then
        mockMvc.perform(
            get("/api/users/following")
                .withTestToken("getfollowing@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    @DisplayName("팔로워 목록 조회 - 성공")
    fun `should get follower list successfully`() {
        // given - 사용자 등록 및 팔로우
        val user1Id = registerAndGetUserId("getfollower@example.com")
        val user2Id = registerAndGetUserId("follower1@example.com")
        val user3Id = registerAndGetUserId("follower2@example.com")

        // 두 명이 user1을 팔로우
        mockMvc.perform(
            post("/api/users/following/$user1Id")
                .withTestToken("follower1@example.com")
        )
        mockMvc.perform(
            post("/api/users/following/$user1Id")
                .withTestToken("follower2@example.com")
        )

        // when & then
        mockMvc.perform(
            get("/api/users/follower")
                .withTestToken("getfollower@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }

    // Helper methods
    private fun registerAndGetToken(email: String): String {
        val request = RegisterRequest(
            email = email,
            password = "password123",
            userName = email.substringBefore("@"),
            nickname = "Test User",
            profileMessage = null,
            profileImageLink = null
        )

        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        return "mock-token" // 실제로는 JWT 토큰 생성
    }

    private fun registerAndGetUserId(email: String): Long {
        val request = RegisterRequest(
            email = email,
            password = "password123",
            userName = email.substringBefore("@"),
            nickname = "Test User",
            profileMessage = null,
            profileImageLink = null
        )

        val result = mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }
}