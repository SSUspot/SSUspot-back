package com.ssuspot.sns.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val claims = jwtTokenProvider.resolveToken(request as HttpServletRequest)
        if (claims != null) {
            SecurityContextHolder.getContext().authentication =
                jwtTokenProvider.getAuthentication(claims)
        }
        filterChain.doFilter(request, response)
    }
}