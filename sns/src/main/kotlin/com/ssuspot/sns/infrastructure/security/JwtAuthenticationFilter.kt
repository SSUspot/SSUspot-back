package com.ssuspot.sns.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.response.security.SecurityErrorResponse
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
) : GenericFilterBean() {
    
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        
        try {
            val authHeader = httpRequest.getHeader("Authorization")
            if (authHeader?.startsWith("Bearer ") == true) {
                val token = authHeader.replace("Bearer ", "")
                
                // 종합적인 토큰 검증 수행 (서명, 만료, 발급시간 모두 검증)
                if (jwtTokenProvider.validateTokenComprehensive(token)) {
                    val claims = jwtTokenProvider.getClaimsFromToken(token)
                    if (claims != null) {
                        SecurityContextHolder.getContext().authentication =
                            jwtTokenProvider.getAuthentication(claims)
                    }
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            // JWT 토큰 관련 예외 처리
            handleJwtException(httpRequest, httpResponse, e)
        }
    }
    
    private fun handleJwtException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: Exception
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        
        val errorCode = when (exception) {
            is ExpiredJwtException -> "EXPIRED_TOKEN"
            is SignatureException -> "INVALID_TOKEN"
            else -> when {
                exception.message?.contains("expired") == true -> "EXPIRED_TOKEN"
                exception.message?.contains("signature") == true -> "INVALID_TOKEN"
                else -> "INVALID_TOKEN"
            }
        }
        
        val errorMessage = when (errorCode) {
            "EXPIRED_TOKEN" -> "인증 토큰이 만료되었습니다."
            "INVALID_TOKEN" -> "유효하지 않은 인증 토큰입니다."
            else -> "토큰 처리 중 오류가 발생했습니다."
        }
        
        val errorResponse = SecurityErrorResponse(
            errorCode = errorCode,
            message = errorMessage
        )
        
        try {
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        } catch (e: IOException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
    }
}