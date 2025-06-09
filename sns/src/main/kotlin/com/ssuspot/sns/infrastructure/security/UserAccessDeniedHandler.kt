package com.ssuspot.sns.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.response.security.SecurityErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class UserAccessDeniedHandler(
    private val objectMapper: ObjectMapper
): AccessDeniedHandler {
    
    @Override
    override fun handle(
        request: HttpServletRequest?, 
        response: HttpServletResponse?, 
        accessDeniedException: org.springframework.security.access.AccessDeniedException?
    ) {
        response?.let { res ->
            res.status = HttpServletResponse.SC_FORBIDDEN
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"
            
            val errorResponse = SecurityErrorResponse(
                errorCode = "ACCESS_DENIED",
                message = "접근 권한이 없습니다."
            )
            
            try {
                res.writer.write(objectMapper.writeValueAsString(errorResponse))
            } catch (e: IOException) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
            }
        }
    }
}