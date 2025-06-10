package com.ssuspot.sns.api.controller.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.request.spot.CreateSpotRequest
import com.ssuspot.sns.api.request.user.LoginRequest
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.support.IntegrationTest
import com.ssuspot.sns.support.helper.DatabaseCleanup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Frontend-Backend 통합 테스트
 * 실제 프론트엔드에서 사용하는 시나리오를 테스트합니다.
 */
@IntegrationTest
@AutoConfigureMockMvc
class IntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val databaseCleanup: DatabaseCleanup
) {
    private var accessToken: String? = null
    private var userId: Long? = null
    private var spotId: Long? = null

    @BeforeEach
    fun setUp() {
        databaseCleanup.execute()
    }

    @Test
    @DisplayName("통합 시나리오 1: 사용자 등록 → 로그인 → 스팟 조회 → 게시물 작성 및 조회")
    fun `complete user journey from registration to posting`() {
        // 1. 사용자 등록
        val registerRequest = RegisterRequest(
            email = "integration@test.com",
            password = "password123",
            userName = "integrationUser",
            nickname = "통합테스트유저",
            profileMessage = "안녕하세요!",
            profileImageLink = null
        )

        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("integration@test.com"))
            .andExpect(jsonPath("$.nickname").value("통합테스트유저"))
            .andDo { result ->
                val response = objectMapper.readTree(result.response.contentAsString)
                userId = response.get("id").asLong()
            }

        // 2. 로그인
        val loginRequest = LoginRequest(
            email = "integration@test.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andDo { result ->
                val response = objectMapper.readTree(result.response.contentAsString)
                accessToken = response.get("accessToken").asText()
            }

        // 3. 스팟 생성
        val createSpotRequest = CreateSpotRequest(
            spotName = "숭실대학교 도서관",
            spotThumbnailImageLink = "https://example.com/library.jpg",
            spotAddress = "서울특별시 동작구 상도로 369",
            spotInfo = "숭실대학교 중앙도서관입니다.",
            spotLevel = 1,
            latitude = 37.496591,
            longitude = 126.957371
        )

        mockMvc.perform(
            post("/api/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSpotRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.spotName").value("숭실대학교 도서관"))
            .andDo { result ->
                val response = objectMapper.readTree(result.response.contentAsString)
                spotId = response.get("id").asLong()
            }

        // 4. 모든 스팟 조회
        mockMvc.perform(
            get("/api/spots")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].spotName").value("숭실대학교 도서관"))

        // 5. 이미지 업로드
        val imageFile = MockMultipartFile(
            "image",
            "test-image.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".toByteArray()
        )

        val imageUploadResult = mockMvc.perform(
            multipart("/api/images")
                .file(imageFile)
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].imageUrl").exists())
            .andReturn()

        val imageResponse = objectMapper.readTree(imageUploadResult.response.contentAsString)
        val imageUrl = imageResponse[0].get("imageUrl").asText()

        // 6. 게시물 작성
        val createPostRequest = CreatePostRequest(
            title = "도서관에서 공부중",
            content = "시험기간이라 도서관에서 열공중입니다!",
            imageUrls = listOf(imageUrl),
            tags = listOf("도서관", "공부", "시험기간"),
            spotId = spotId!!
        )

        var postId: Long? = null
        mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("도서관에서 공부중"))
            .andExpect(jsonPath("$.content").value("시험기간이라 도서관에서 열공중입니다!"))
            .andExpect(jsonPath("$.images").isArray)
            .andExpect(jsonPath("$.tags").isArray)
            .andExpect(jsonPath("$.spot.id").value(spotId))
            .andDo { result ->
                val response = objectMapper.readTree(result.response.contentAsString)
                postId = response.get("id").asLong()
            }

        // 7. 스팟별 게시물 조회
        mockMvc.perform(
            get("/api/posts/spots/$spotId")
                .header("Authorization", "Bearer $accessToken")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].title").value("도서관에서 공부중"))

        // 8. 게시물 좋아요
        mockMvc.perform(
            post("/api/posts/$postId/likes")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.pressed").value(true))

        // 9. 게시물 상세 조회
        mockMvc.perform(
            get("/api/posts/$postId")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.hasLiked").value(true))
            .andExpect(jsonPath("$.likeCount").value(1))
    }

    @Test
    @DisplayName("통합 시나리오 2: 팔로우 시스템 및 피드 조회")
    fun `follow system and feed integration test`() {
        // 1. 두 명의 사용자 등록
        val user1Email = "user1@test.com"
        val user2Email = "user2@test.com"
        
        // User 1 등록
        registerUser(user1Email, "유저1")
        val user1Token = loginAndGetToken(user1Email)
        
        // User 2 등록
        registerUser(user2Email, "유저2")
        val user2Token = loginAndGetToken(user2Email)
        val user2Id = getUserId(user2Email, user2Token)

        // 스팟 생성
        createTestSpot()

        // 2. User2가 게시물 작성
        val postRequest = CreatePostRequest(
            title = "유저2의 첫 게시물",
            content = "팔로우 테스트를 위한 게시물입니다.",
            imageUrls = emptyList(),
            tags = listOf("테스트"),
            spotId = spotId!!
        )

        mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $user2Token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
        )
            .andExpect(status().isOk)

        // 3. User1이 팔로잉 피드 조회 (팔로우 전 - 비어있어야 함)
        mockMvc.perform(
            get("/api/posts")
                .header("Authorization", "Bearer $user1Token")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))

        // 4. User1이 User2를 팔로우
        mockMvc.perform(
            post("/api/users/following/$user2Id")
                .header("Authorization", "Bearer $user1Token")
        )
            .andExpect(status().isOk)

        // 5. User1의 팔로잉 목록 확인
        mockMvc.perform(
            get("/api/users/following")
                .header("Authorization", "Bearer $user1Token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].nickname").value("유저2"))

        // 6. User1이 팔로잉 피드 조회 (팔로우 후 - User2의 게시물이 보여야 함)
        mockMvc.perform(
            get("/api/posts")
                .header("Authorization", "Bearer $user1Token")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].title").value("유저2의 첫 게시물"))
    }

    @Test
    @DisplayName("통합 시나리오 3: 댓글 시스템 테스트")
    fun `comment system integration test`() {
        // 1. 사용자 등록 및 로그인
        registerUser("commenter@test.com", "댓글러")
        val token = loginAndGetToken("commenter@test.com")
        
        // 2. 스팟 및 게시물 생성
        createTestSpot()
        val postId = createTestPost(token)

        // 3. 댓글 작성
        val commentRequest = mapOf("content" to "정말 좋은 게시물이네요!")
        
        var commentId: Long? = null
        mockMvc.perform(
            post("/api/posts/$postId/comments")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").value("정말 좋은 게시물이네요!"))
            .andExpect(jsonPath("$.user.nickname").value("댓글러"))
            .andDo { result ->
                val response = objectMapper.readTree(result.response.contentAsString)
                commentId = response.get("id").asLong()
            }

        // 4. 댓글 목록 조회
        mockMvc.perform(
            get("/api/posts/$postId/comments")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].content").value("정말 좋은 게시물이네요!"))

        // 5. 댓글 수정
        val updateRequest = mapOf("content" to "수정된 댓글입니다!")
        
        mockMvc.perform(
            put("/api/comments/$commentId")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").value("수정된 댓글입니다!"))

        // 6. 댓글 삭제
        mockMvc.perform(
            delete("/api/comments/$commentId")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)

        // 7. 댓글 목록 재조회 (비어있어야 함)
        mockMvc.perform(
            get("/api/posts/$postId/comments")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    @DisplayName("통합 시나리오 4: 태그 검색 테스트")
    fun `tag search integration test`() {
        // 1. 사용자 등록 및 로그인
        registerUser("tagger@test.com", "태거")
        val token = loginAndGetToken("tagger@test.com")
        
        // 2. 스팟 생성
        createTestSpot()

        // 3. 다양한 태그를 가진 게시물 생성
        val posts = listOf(
            CreatePostRequest(
                title = "봄 꽃구경",
                content = "벚꽃이 만개했어요",
                imageUrls = emptyList(),
                tags = listOf("봄", "벚꽃", "꽃구경"),
                spotId = spotId!!
            ),
            CreatePostRequest(
                title = "가을 단풍",
                content = "단풍이 예쁘네요",
                imageUrls = emptyList(),
                tags = listOf("가을", "단풍"),
                spotId = spotId!!
            ),
            CreatePostRequest(
                title = "봄 소풍",
                content = "날씨가 좋아요",
                imageUrls = emptyList(),
                tags = listOf("봄", "소풍"),
                spotId = spotId!!
            )
        )

        posts.forEach { postRequest ->
            mockMvc.perform(
                post("/api/posts")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postRequest))
            )
                .andExpect(status().isOk)
        }

        // 4. 태그로 검색 - "봄" 태그
        mockMvc.perform(
            get("/api/tags")
                .header("Authorization", "Bearer $token")
                .param("tagName", "봄")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].tags[*]", org.hamcrest.Matchers.hasItem("봄")))
            .andExpect(jsonPath("$[1].tags[*]", org.hamcrest.Matchers.hasItem("봄")))
    }

    // Helper methods
    private fun registerUser(email: String, nickname: String) {
        val registerRequest = RegisterRequest(
            email = email,
            password = "password123",
            userName = email.substringBefore("@"),
            nickname = nickname,
            profileMessage = null,
            profileImageLink = null
        )

        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isOk)
    }

    private fun loginAndGetToken(email: String): String {
        val loginRequest = LoginRequest(
            email = email,
            password = "password123"
        )

        val result = mockMvc.perform(
            post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("accessToken").asText()
    }

    private fun getUserId(email: String, token: String): Long {
        val result = mockMvc.perform(
            get("/api/users")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }

    private fun createTestSpot() {
        val createSpotRequest = CreateSpotRequest(
            spotName = "테스트 스팟",
            spotThumbnailImageLink = "https://example.com/spot.jpg",
            spotAddress = "테스트 주소",
            spotInfo = "테스트 스팟입니다.",
            spotLevel = 1,
            latitude = 37.5,
            longitude = 127.0
        )

        val result = mockMvc.perform(
            post("/api/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSpotRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        spotId = response.get("id").asLong()
    }

    private fun createTestPost(token: String): Long {
        val postRequest = CreatePostRequest(
            title = "테스트 게시물",
            content = "테스트 내용입니다.",
            imageUrls = emptyList(),
            tags = listOf("테스트"),
            spotId = spotId!!
        )

        val result = mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        return response.get("id").asLong()
    }
}