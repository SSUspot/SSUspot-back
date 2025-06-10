package com.ssuspot.sns.infrastructure.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

/**
 * 보안 헤더를 추가하는 필터
 * OWASP 권장사항에 따른 보안 헤더들을 응답에 추가합니다.
 */
@Component
class SecurityHeadersFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        
        // Content Security Policy
        // XSS 공격을 방지하고 리소스 로딩을 제한합니다
        val cspHeader = buildString {
            append("default-src 'self'; ")
            append("script-src 'self' 'unsafe-inline' 'unsafe-eval'; ") // 개발 편의를 위해 일부 허용
            append("style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; ")
            append("font-src 'self' https://fonts.gstatic.com; ")
            append("img-src 'self' data: https: blob:; ") // 이미지 업로드를 위한 blob: 허용
            append("connect-src 'self' https:; ")
            append("media-src 'self' https:; ")
            append("object-src 'none'; ")
            append("frame-src 'none'; ")
            append("base-uri 'self'; ")
            append("form-action 'self'; ")
            append("upgrade-insecure-requests;")
        }
        httpResponse.setHeader("Content-Security-Policy", cspHeader)
        
        // X-Content-Type-Options
        // MIME 타입 스니핑을 방지합니다
        httpResponse.setHeader("X-Content-Type-Options", "nosniff")
        
        // X-Frame-Options
        // 클릭재킹 공격을 방지합니다
        httpResponse.setHeader("X-Frame-Options", "DENY")
        
        // X-XSS-Protection
        // 브라우저의 XSS 필터를 활성화합니다
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block")
        
        // Referrer-Policy
        // 리퍼러 정보 노출을 제한합니다
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin")
        
        // Permissions-Policy (Feature-Policy의 후속)
        // 브라우저 기능 사용을 제한합니다
        val permissionsPolicy = buildString {
            append("geolocation=(), ")
            append("microphone=(), ")
            append("camera=(), ")
            append("payment=(), ")
            append("usb=(), ")
            append("magnetometer=(), ")
            append("gyroscope=(), ")
            append("speaker=()")
        }
        httpResponse.setHeader("Permissions-Policy", permissionsPolicy)
        
        // Strict-Transport-Security (HTTPS 환경에서만)
        // HTTPS를 강제하고 다운그레이드 공격을 방지합니다
        if (httpRequest.isSecure || isProxiedHttps(httpRequest)) {
            httpResponse.setHeader(
                "Strict-Transport-Security", 
                "max-age=31536000; includeSubDomains; preload"
            )
        }
        
        // Cross-Origin-Embedder-Policy
        // 크로스 오리진 리소스 임베딩을 제한합니다
        httpResponse.setHeader("Cross-Origin-Embedder-Policy", "require-corp")
        
        // Cross-Origin-Opener-Policy
        // 크로스 오리진 창 접근을 제한합니다
        httpResponse.setHeader("Cross-Origin-Opener-Policy", "same-origin")
        
        // Cross-Origin-Resource-Policy
        // 크로스 오리진 리소스 접근을 제한합니다
        httpResponse.setHeader("Cross-Origin-Resource-Policy", "cross-origin")
        
        // Cache-Control for sensitive endpoints
        if (isSensitiveEndpoint(httpRequest)) {
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
            httpResponse.setHeader("Pragma", "no-cache")
            httpResponse.setHeader("Expires", "0")
        }
        
        // Server header removal (보안을 위해 서버 정보 숨김)
        httpResponse.setHeader("Server", "")
        
        // X-Powered-By header removal (보안을 위해 기술 스택 정보 숨김)
        httpResponse.setHeader("X-Powered-By", "")
        
        chain.doFilter(request, response)
    }
    
    /**
     * 프록시를 통한 HTTPS 연결인지 확인합니다.
     */
    private fun isProxiedHttps(request: HttpServletRequest): Boolean {
        val xForwardedProto = request.getHeader("X-Forwarded-Proto")
        val xForwardedSsl = request.getHeader("X-Forwarded-Ssl")
        val xUrlScheme = request.getHeader("X-Url-Scheme")
        
        return "https".equals(xForwardedProto, ignoreCase = true) ||
               "on".equals(xForwardedSsl, ignoreCase = true) ||
               "https".equals(xUrlScheme, ignoreCase = true)
    }
    
    /**
     * 민감한 엔드포인트인지 확인합니다.
     */
    private fun isSensitiveEndpoint(request: HttpServletRequest): Boolean {
        val uri = request.requestURI.lowercase()
        val sensitiveEndpoints = listOf(
            "/api/auth/login",
            "/api/auth/refresh", 
            "/api/auth/register",
            "/api/users/me",
            "/actuator"
        )
        
        return sensitiveEndpoints.any { uri.startsWith(it) }
    }
}