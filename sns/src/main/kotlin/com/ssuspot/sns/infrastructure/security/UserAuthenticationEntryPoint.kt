package com.ssuspot.sns.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.response.security.SecurityErrorResponse
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

@Component
class UserAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
): AuthenticationEntryPoint {
    
    @Override
    override fun commence(
        request: HttpServletRequest?, 
        response: HttpServletResponse?, 
        authException: org.springframework.security.core.AuthenticationException?
    ) {
        response?.let { res ->
            res.status = HttpServletResponse.SC_UNAUTHORIZED
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"
            
            val errorCode = determineErrorCode(authException, request)
            val errorResponse = SecurityErrorResponse(
                errorCode = errorCode,
                message = determineErrorMessage(errorCode, authException)
            )
            
            try {
                res.writer.write(objectMapper.writeValueAsString(errorResponse))
            } catch (e: IOException) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            }
        }
    }
    
    private fun determineErrorCode(
        authException: org.springframework.security.core.AuthenticationException?,
        request: HttpServletRequest?
    ): String {
        val authHeader = request?.getHeader("Authorization")
        
        return when {
            authHeader?.startsWith("Bearer ") == true -> {
                when (authException?.message) {
                    "JWT token has expired" -> "EXPIRED_TOKEN"
                    "JWT signature does not match locally computed signature" -> "INVALID_TOKEN"
                    else -> "INVALID_TOKEN"
                }
            }
            authHeader == null -> "AUTHENTICATION_REQUIRED"
            else -> "UNAUTHORIZED"
        }
    }
    
    private fun determineErrorMessage(errorCode: String, authException: org.springframework.security.core.AuthenticationException?): String {
        return when (errorCode) {
            "EXPIRED_TOKEN" -> "인증 토큰이 만료되었습니다."
            "INVALID_TOKEN" -> "유효하지 않은 인증 토큰입니다."
            "AUTHENTICATION_REQUIRED" -> "인증이 필요한 요청입니다."
            else -> authException?.message ?: "인증이 실패했습니다."
        }
    }
}