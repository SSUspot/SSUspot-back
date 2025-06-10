package com.ssuspot.sns.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.controller.user.UserController
import com.ssuspot.sns.api.request.user.RegisterRequest
import com.ssuspot.sns.application.dto.user.UserResponseDto
import com.ssuspot.sns.application.service.user.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(UserController::class)
@ActiveProfiles("test")
@DisplayName("UserController MockMvc 테스트")
class UserControllerMockTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        // Mock 설정
    }

    @Test
    @DisplayName("회원가입이 성공한다")
    fun registerUser_success() {
        // given
        val registerRequest = RegisterRequest(
            email = "test@example.com",
            password = "Password123!",
            userName = "testuser",
            nickname = "테스트유저",
            profileMessage = "안녕하세요",
            profileImageLink = null
        )

        val userResponse = UserResponseDto(
            id = 1L,
            email = "test@example.com",
            userName = "testuser",
            nickname = "테스트유저",
            profileMessage = "안녕하세요",
            profileImageLink = null,
            createdAt = LocalDateTime.now()
        )

        whenever(userService.register(any())).thenReturn(userResponse)

        // when & then
        mockMvc.perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.userName").value("testuser"))
            .andExpect(jsonPath("$.nickname").value("테스트유저"))
    }
}