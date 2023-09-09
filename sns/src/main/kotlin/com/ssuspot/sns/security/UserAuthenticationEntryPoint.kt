package com.ssuspot.sns.security

import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserAuthenticationEntryPoint: AuthenticationEntryPoint {
    @Override
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: org.springframework.security.core.AuthenticationException?) {
        response?.sendError(401, "Unauthorized")
    }
}