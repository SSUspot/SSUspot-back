package com.ssuspot.sns.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.support.IntegrationTest
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

@IntegrationTest
@DisplayName("JWT 인증 필터 테스트")
class JwtAuthenticationFilterTest {

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
    private lateinit var filterChain: FilterChain
    
    private val testEmail = "test@example.com"
    
    @BeforeEach
    fun setUp() {
        jwtAuthenticationFilter = JwtAuthenticationFilter(jwtTokenProvider, objectMapper)
        filterChain = mockk(relaxed = true)
        SecurityContextHolder.clearContext()
    }
    
    @Test
    @DisplayName("유효한 JWT 토큰으로 인증에 성공한다")
    fun doFilter_WithValidToken_Success() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        request.addHeader("Authorization", "Bearer ${token.token}")
        
        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)
        
        // then
        verify { filterChain.doFilter(request, response) }
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNotNull
        assertThat(authentication.principal).isEqualTo(testEmail)
    }
    
    @Test
    @DisplayName("Authorization 헤더가 없으면 필터를 통과한다")
    fun doFilter_WithoutAuthHeader_PassThrough() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        
        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)
        
        // then
        verify { filterChain.doFilter(request, response) }
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNull()
    }
    
    @Test
    @DisplayName("인증 성공 후 SecurityContext에 Authentication이 저장된다")
    fun doFilter_SuccessfulAuth_SetsSecurityContext() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        request.addHeader("Authorization", "Bearer ${token.token}")
        
        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)
        
        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNotNull
        assertThat(authentication.isAuthenticated).isTrue()
        assertThat(authentication.principal).isEqualTo(testEmail)
        assertThat(authentication.authorities).isEmpty()
    }
    
    @Test
    @DisplayName("Bearer 프리픽스가 없는 토큰은 인증을 설정하지 않고 요청을 계속 처리한다")
    fun doFilter_WithoutBearerPrefix_SkipsAuthentication() {
        // given
        val token = jwtTokenProvider.generateAccessToken(testEmail)
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        request.addHeader("Authorization", token.token)
        
        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)
        
        // then
        // SecurityContext에 인증 정보가 설정되지 않아야 함
        assertThat(SecurityContextHolder.getContext().authentication).isNull()
        verify(exactly = 1) { filterChain.doFilter(request, response) }
    }
}