package com.ssuspot.sns.infrastructure.configs

import com.ssuspot.sns.support.IntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@IntegrationTest
@DisplayName("CORS 설정 테스트")
class WebConfigTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("CORS 프리플라이트 요청이 성공한다")
    fun cors_PreflightRequest_Success() {
        mockMvc.perform(options("/api/posts")
            .header("Origin", "http://localhost:3000")
            .header("Access-Control-Request-Method", "POST")
            .header("Access-Control-Request-Headers", "Authorization,Content-Type"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
            .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PATCH,DELETE,OPTIONS"))
            .andExpect(header().string("Access-Control-Allow-Headers", "Authorization, Content-Type"))
    }

    @Test
    @DisplayName("허용된 Origin에서 실제 요청이 성공한다")
    fun cors_ActualRequest_WithAllowedOrigin_Success() {
        mockMvc.perform(get("/api/spots")
            .header("Origin", "http://localhost:3000"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
    }

    @Test
    @DisplayName("허용되지 않은 Origin에서 요청 시 CORS 에러 발생")
    fun cors_ActualRequest_WithDisallowedOrigin_NoCorsHeader() {
        mockMvc.perform(get("/api/spots")
            .header("Origin", "http://malicious-site.com"))
            .andExpect(status().isForbidden())
            .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
    }

    @Test
    @DisplayName("CORS 헤더에 Credentials 허용이 설정되어 있다")
    fun cors_CredentialsAllowed() {
        mockMvc.perform(options("/api/posts")
            .header("Origin", "http://localhost:3000")
            .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
    }

    @Test
    @DisplayName("CORS 최대 나이가 설정되어 있다")
    fun cors_MaxAgeSet() {
        mockMvc.perform(options("/api/posts")
            .header("Origin", "http://localhost:3000")
            .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk())
            .andExpect(header().exists("Access-Control-Max-Age"))
    }
}