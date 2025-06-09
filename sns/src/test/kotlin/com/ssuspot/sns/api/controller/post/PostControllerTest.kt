package com.ssuspot.sns.api.controller.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.request.post.UpdatePostRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.infrastructure.repository.spot.SpotRepository
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.support.IntegrationTest
import com.ssuspot.sns.support.fixture.SpotFixture
import com.ssuspot.sns.support.helper.DatabaseCleanup
import com.ssuspot.sns.support.helper.JwtTestHelper.withTestToken
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
class PostControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val databaseCleanup: DatabaseCleanup,
    private val spotRepository: SpotRepository,
    private val userRepository: UserRepository
) {

    private lateinit var testSpot: Spot
    private var testUserId: Long = 0

    @BeforeEach
    fun setUp() {
        databaseCleanup.execute()
        
        // 테스트용 Spot 생성
        testSpot = spotRepository.save(SpotFixture.createSpot())
        
        // 테스트용 사용자 생성
        testUserId = createTestUser("posttest@example.com")
    }

    @Test
    @DisplayName("게시글 생성 - 성공")
    fun `should create post successfully`() {
        // given
        val request = CreatePostRequest(
            title = "Test Post Title",
            content = "This is a test post content",
            imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
            tags = listOf("test", "spring"),
            spotId = testSpot.id!!
        )

        // when & then
        mockMvc.perform(
            post("/api/posts")
                .withTestToken("posttest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Test Post Title"))
            .andExpect(jsonPath("$.content").value("This is a test post content"))
            .andExpect(jsonPath("$.spotId").value(testSpot.id))
            .andExpect(jsonPath("$.imageUrls.length()").value(2))
            .andExpect(jsonPath("$.tags.length()").value(2))
            .andExpect(jsonPath("$.id").exists())
    }

    @Test
    @DisplayName("게시글 생성 - 존재하지 않는 Spot ID로 실패")
    fun `should fail to create post with invalid spot id`() {
        // given
        val request = CreatePostRequest(
            title = "Test Post Title",
            content = "This is a test post content",
            imageUrls = listOf(),
            tags = listOf(),
            spotId = 999999L // 존재하지 않는 Spot ID
        )

        // when & then
        mockMvc.perform(
            post("/api/posts")
                .withTestToken("posttest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("게시글 조회 - ID로 조회 성공")
    fun `should get post by id successfully`() {
        // given - 먼저 게시글 생성
        val postId = createTestPost()

        // when & then
        mockMvc.perform(
            get("/api/posts/$postId")
                .withTestToken("posttest@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(postId))
            .andExpect(jsonPath("$.title").exists())
            .andExpect(jsonPath("$.content").exists())
    }

    @Test
    @DisplayName("게시글 조회 - 존재하지 않는 ID로 실패")
    fun `should fail to get non-existent post`() {
        // when & then
        mockMvc.perform(
            get("/api/posts/999999")
                .withTestToken("posttest@example.com")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @DisplayName("게시글 수정 - 성공")
    fun `should update post successfully`() {
        // given - 먼저 게시글 생성
        val postId = createTestPost()
        
        val updateRequest = UpdatePostRequest(
            title = "Updated Post Title",
            content = "Updated post content",
            tags = listOf("updated", "tags")
        )

        // when & then
        mockMvc.perform(
            patch("/api/posts/$postId")
                .withTestToken("posttest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated Post Title"))
            .andExpect(jsonPath("$.content").value("Updated post content"))
            .andExpect(jsonPath("$.tags.length()").value(2))
    }

    @Test
    @DisplayName("게시글 수정 - 다른 사용자의 게시글 수정 실패")
    fun `should fail to update other user's post`() {
        // given - 다른 사용자로 게시글 생성
        val postId = createTestPost()
        createTestUser("otheruser@example.com")
        
        val updateRequest = UpdatePostRequest(
            title = "Updated Post Title",
            content = "Updated post content",
            tags = listOf()
        )

        // when & then
        mockMvc.perform(
            patch("/api/posts/$postId")
                .withTestToken("otheruser@example.com") // 다른 사용자
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("게시글 삭제 - 성공")
    fun `should delete post successfully`() {
        // given - 먼저 게시글 생성
        val postId = createTestPost()

        // when & then
        mockMvc.perform(
            delete("/api/posts/$postId")
                .withTestToken("posttest@example.com")
        )
            .andExpect(status().isOk)
        
        // 삭제 확인
        mockMvc.perform(
            get("/api/posts/$postId")
                .withTestToken("posttest@example.com")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @DisplayName("게시글 삭제 - 다른 사용자의 게시글 삭제 실패")
    fun `should fail to delete other user's post`() {
        // given - 게시글 생성
        val postId = createTestPost()
        createTestUser("otheruser2@example.com")

        // when & then
        mockMvc.perform(
            delete("/api/posts/$postId")
                .withTestToken("otheruser2@example.com") // 다른 사용자
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("내 게시글 목록 조회 - 성공")
    fun `should get my posts successfully`() {
        // given - 여러 개의 게시글 생성
        createTestPost()
        createTestPost()
        createTestPost()

        // when & then
        mockMvc.perform(
            get("/api/posts/users/me")
                .withTestToken("posttest@example.com")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    @DisplayName("특정 사용자의 게시글 목록 조회 - 성공")
    fun `should get user posts successfully`() {
        // given
        createTestPost()
        createTestPost()

        // when & then
        mockMvc.perform(
            get("/api/posts/users/$testUserId")
                .withTestToken("posttest@example.com")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    @DisplayName("특정 Spot의 게시글 목록 조회 - 성공")
    fun `should get posts by spot id successfully`() {
        // given
        createTestPost()
        createTestPost()
        
        // 다른 Spot에 게시글 생성
        val otherSpot = spotRepository.save(SpotFixture.createSpot(id = null, spotName = "Other Spot"))
        createTestPostWithSpot(otherSpot.id!!)

        // when & then
        mockMvc.perform(
            get("/api/posts/spots/${testSpot.id}")
                .withTestToken("posttest@example.com")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2)) // testSpot의 게시글만
    }

    @Test
    @DisplayName("태그로 게시글 검색 - 성공")
    fun `should search posts by tag successfully`() {
        // given
        createTestPostWithTags(listOf("spring", "boot"))
        createTestPostWithTags(listOf("spring", "jpa"))
        createTestPostWithTags(listOf("kotlin", "test"))

        // when & then
        mockMvc.perform(
            get("/api/tags")
                .withTestToken("posttest@example.com")
                .param("tagName", "spring")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2)) // "spring" 태그가 있는 게시글만
    }

    @Test
    @DisplayName("팔로잉한 사용자들의 게시글 조회 - 성공")
    fun `should get following users posts successfully`() {
        // given - 다른 사용자들 생성 및 팔로우
        val user2Id = createTestUser("followed1@example.com")
        val user3Id = createTestUser("followed2@example.com")
        
        // 팔로우
        mockMvc.perform(
            post("/api/users/following/$user2Id")
                .withTestToken("posttest@example.com")
        )
        mockMvc.perform(
            post("/api/users/following/$user3Id")
                .withTestToken("posttest@example.com")
        )
        
        // 팔로우한 사용자들이 게시글 작성
        createTestPostByUser("followed1@example.com")
        createTestPostByUser("followed2@example.com")
        
        // 팔로우하지 않은 사용자의 게시글
        createTestUser("notfollowed@example.com")
        createTestPostByUser("notfollowed@example.com")

        // when & then
        mockMvc.perform(
            get("/api/posts")
                .withTestToken("posttest@example.com")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2)) // 팔로우한 사용자들의 게시글만
    }

    @Test
    @DisplayName("추천 게시글 조회 - 성공")
    fun `should get recommended posts successfully`() {
        // given - 여러 게시글 생성
        createTestPost()
        createTestPost()

        // when & then
        mockMvc.perform(
            get("/api/posts/recommend")
                .withTestToken("posttest@example.com")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
    }

    // Helper methods
    private fun createTestUser(email: String): Long {
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

    private fun createTestPost(): Long {
        val request = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            imageUrls = listOf(),
            tags = listOf("test"),
            spotId = testSpot.id!!
        )

        val result = mockMvc.perform(
            post("/api/posts")
                .withTestToken("posttest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }

    private fun createTestPostWithSpot(spotId: Long): Long {
        val request = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            imageUrls = listOf(),
            tags = listOf(),
            spotId = spotId
        )

        val result = mockMvc.perform(
            post("/api/posts")
                .withTestToken("posttest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }

    private fun createTestPostWithTags(tags: List<String>): Long {
        val request = CreatePostRequest(
            title = "Test Post",
            content = "Test Content",
            imageUrls = listOf(),
            tags = tags,
            spotId = testSpot.id!!
        )

        val result = mockMvc.perform(
            post("/api/posts")
                .withTestToken("posttest@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }

    private fun createTestPostByUser(email: String): Long {
        val request = CreatePostRequest(
            title = "Test Post by $email",
            content = "Test Content",
            imageUrls = listOf(),
            tags = listOf(),
            spotId = testSpot.id!!
        )

        val result = mockMvc.perform(
            post("/api/posts")
                .withTestToken(email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }
}