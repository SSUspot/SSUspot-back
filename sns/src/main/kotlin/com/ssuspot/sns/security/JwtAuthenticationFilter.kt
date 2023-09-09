package com.ssuspot.sns.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.core.common.model.dto.ErrorDto
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.SignatureException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException

class JwtAuthenticationFilter(
        private val jwtTokenProvider: JwtTokenProvider,
        private val objectMapper: ObjectMapper
) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        try {
            val claims = jwtTokenProvider.resolveToken(request as HttpServletRequest)
            if (claims != null) {
                SecurityContextHolder.getContext().authentication =
                        jwtTokenProvider.getAuthentication(claims)
            }
            filterChain.doFilter(request, response)
        } catch (e: SignatureException) {
            sendErrorMessage(response as HttpServletResponse, "Auth-001", "유효하지 않은 토큰입니다.")
        } catch (e: MalformedJwtException) {
            sendErrorMessage(response as HttpServletResponse, "Auth-002", "손상된 토큰입니다.")
        } catch (e: DecodingException) {
            sendErrorMessage(response as HttpServletResponse, "Auth-003", "잘못된 인증입니다.")
        } catch (e: ExpiredJwtException) {
            sendErrorMessage(response as HttpServletResponse, "Auth-004", "만료된 토큰입니다.")
        }
    }

    @Throws(IOException::class)
    private fun sendErrorMessage(res: HttpServletResponse, error: String, message: String) {
        res.status = HttpServletResponse.SC_UNAUTHORIZED
        res.contentType = MediaType.APPLICATION_JSON.toString()
        res.characterEncoding = "UTF-8"
        res.writer.write(objectMapper.writeValueAsString(ErrorDto(HttpStatus.FORBIDDEN, error, message)))
    }
}