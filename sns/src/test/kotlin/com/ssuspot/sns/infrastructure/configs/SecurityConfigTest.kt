package com.ssuspot.sns.infrastructure.configs

import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import com.ssuspot.sns.support.IntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@IntegrationTest
@DisplayName("Security 설정 테스트")
class SecurityConfigTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    @WithAnonymousUser
    @DisplayName("공개 엔드포인트는 인증 없이 접근 가능하다")
    fun publicEndpoints_WithoutAuth_Allowed() {
        // 공개 엔드포인트들은 인증 없이 접근 가능
        mockMvc.perform(post("/api/users/register")
            .contentType("application/json")
            .content("{}"))
            .andExpect(status().isBadRequest()) // 컨트롤러까지 도달, 비즈니스 로직에서 400
            
        mockMvc.perform(post("/api/users/login")
            .contentType("application/json")
            .content("{}"))
            .andExpect(status().isBadRequest()) // 컨트롤러까지 도달, 비즈니스 로직에서 400
            
        mockMvc.perform(get("/api/spots"))
            .andExpect(status().isOk()) // 성공적으로 접근
    }

    @Test
    @WithAnonymousUser
    @DisplayName("보호된 엔드포인트는 인증 없이 접근 시 403 반환")
    fun protectedEndpoints_WithoutAuth_Returns403() {
        // 보호된 엔드포인트들은 인증 없이 접근 시 403 반환 (Spring Security 기본 동작)
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isForbidden())
            
        mockMvc.perform(get("/api/alarms"))
            .andExpect(status().isForbidden())
            
        mockMvc.perform(post("/api/posts")
            .contentType("application/json")
            .content("{}"))
            .andExpect(status().isForbidden())
    }

    @Test
    @DisplayName("CORS가 활성화되어 있다")
    fun cors_IsEnabled() {
        mockMvc.perform(options("/api/posts")
            .header("Origin", "http://localhost:3000")
            .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk())
            .andExpect(header().exists("Access-Control-Allow-Origin"))
    }

    @Test
    @DisplayName("CSRF가 비활성화되어 있다")
    fun csrf_IsDisabled() {
        // CSRF가 활성화되어 있다면 POST 요청 시 403 Forbidden이 발생함
        // 공개 엔드포인트로 테스트 (로그인은 공개 엔드포인트이므로 CSRF 영향 확인 가능)
        mockMvc.perform(post("/api/users/login")
            .contentType("application/json")
            .content("{}"))
            .andExpect(status().isBadRequest()) // CSRF가 비활성화되어 있어서 컨트롤러까지 도달
    }

    @Test
    @DisplayName("세션이 생성되지 않는다 (STATELESS)")
    fun session_IsStateless() {
        mockMvc.perform(get("/api/test"))
            .andExpect { result ->
                val session = result.request.getSession(false)
                assert(session == null) { "Session should not be created" }
            }
    }

    @Test
    @DisplayName("JWT 필터가 필터 체인에 추가되어 있다")
    fun jwtFilter_IsInFilterChain() {
        // JWT 필터가 작동하는지 확인 - 잘못된 토큰으로 DecodingException 발생
        org.junit.jupiter.api.Assertions.assertThrows(Exception::class.java) {
            mockMvc.perform(get("/api/test")
                .header("Authorization", "invalid-token"))
        }
    }

    @Test
    @DisplayName("유효한 JWT 토큰으로 보호된 엔드포인트에 접근 가능하다")
    fun protectedEndpoints_WithValidToken_Allowed() {
        // JWT 토큰 생성
        val token = jwtTokenProvider.generateAccessToken("test@example.com")
        
        // 보호된 엔드포인트에 유효한 토큰으로 접근
        mockMvc.perform(get("/api/users")
            .header("Authorization", "Bearer ${token.token}"))
            .andExpect(status().isBadRequest()) // 컨트롤러까지 도달, 비즈니스 로직에서 400
            
        mockMvc.perform(get("/api/alarms")
            .header("Authorization", "Bearer ${token.token}"))
            .andExpect(status().isBadRequest()) // 컨트롤러까지 도달, 비즈니스 로직에서 400
    }

    @Test
    @DisplayName("HTTP Basic 인증이 비활성화되어 있다")
    fun httpBasic_IsDisabled() {
        // HTTP Basic 인증 헤더로 요청해도 인증되지 않고 JWT 필터에서 DecodingException 발생
        org.junit.jupiter.api.Assertions.assertThrows(Exception::class.java) {
            mockMvc.perform(get("/api/users")
                .header("Authorization", "Basic dGVzdDp0ZXN0")) // test:test in Base64
        }
    }
}