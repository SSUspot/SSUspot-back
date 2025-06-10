package com.ssuspot.sns.infrastructure.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.UUID

/**
 * HTTP 요청/응답 로깅을 위한 인터셉터
 * 모든 API 요청에 대한 로그를 구조화하여 기록합니다.
 */
@Component
class LoggingInterceptor(
    private val loggerFactory: StructuredLoggerFactory
) : HandlerInterceptor {

    companion object {
        private const val START_TIME_ATTRIBUTE = "startTime"
        private const val REQUEST_SIZE_ATTRIBUTE = "requestSize"
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val startTime = System.currentTimeMillis()
        request.setAttribute(START_TIME_ATTRIBUTE, startTime)

        // 요청 크기 계산 (대략적)
        val requestSize = calculateRequestSize(request)
        request.setAttribute(REQUEST_SIZE_ATTRIBUTE, requestSize)

        // 요청 컨텍스트 설정
        loggerFactory.setRequestContext(
            traceId = UUID.randomUUID().toString(),
            spanId = UUID.randomUUID().toString(),
            clientIp = getClientIpAddress(request),
            userAgent = request.getHeader("User-Agent"),
            requestUri = request.requestURI,
            httpMethod = request.method
        )

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        // 후처리 로직이 필요한 경우 여기에 추가
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        try {
            val startTime = request.getAttribute(START_TIME_ATTRIBUTE) as? Long ?: return
            val duration = System.currentTimeMillis() - startTime
            val requestSize = request.getAttribute(REQUEST_SIZE_ATTRIBUTE) as? Long
            val responseSize = calculateResponseSize(response)

            // API 호출 로그 기록
            loggerFactory.logApiCall(
                endpoint = request.requestURI,
                method = request.method,
                statusCode = response.status,
                duration = duration,
                clientIp = getClientIpAddress(request),
                userAgent = request.getHeader("User-Agent"),
                requestSize = requestSize,
                responseSize = responseSize
            )

            // 예외가 발생한 경우 보안 로그 기록
            ex?.let {
                loggerFactory.logSecurity(
                    event = SecurityEvent.SUSPICIOUS_ACTIVITY,
                    clientIp = getClientIpAddress(request),
                    details = mapOf(
                        "endpoint" to request.requestURI,
                        "method" to request.method,
                        "exception" to it.javaClass.simpleName,
                        "message" to (it.message ?: "Unknown error")
                    )
                )
            }

            // 느린 요청에 대한 성능 로그
            if (duration > 5000) { // 5초 이상
                loggerFactory.logPerformance(
                    operation = "SLOW_REQUEST",
                    duration = duration,
                    success = response.status < 400,
                    details = mapOf(
                        "endpoint" to request.requestURI,
                        "method" to request.method,
                        "statusCode" to response.status
                    )
                )
            }

        } finally {
            // MDC 컨텍스트 정리
            loggerFactory.clearContext()
        }
    }

    /**
     * 클라이언트 IP 주소를 추출합니다.
     */
    private fun getClientIpAddress(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        val xRealIp = request.getHeader("X-Real-IP")
        val xOriginalForwardedFor = request.getHeader("X-Original-Forwarded-For")
        val cfConnectingIp = request.getHeader("CF-Connecting-IP") // Cloudflare

        return when {
            !xForwardedFor.isNullOrBlank() -> xForwardedFor.split(",")[0].trim()
            !cfConnectingIp.isNullOrBlank() -> cfConnectingIp
            !xRealIp.isNullOrBlank() -> xRealIp
            !xOriginalForwardedFor.isNullOrBlank() -> xOriginalForwardedFor.split(",")[0].trim()
            else -> request.remoteAddr
        }
    }

    /**
     * 요청 크기를 대략적으로 계산합니다.
     */
    private fun calculateRequestSize(request: HttpServletRequest): Long {
        var size = 0L
        
        // Headers
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            val headerValue = request.getHeader(headerName)
            size += headerName.length + (headerValue?.length ?: 0)
        }
        
        // Query string
        request.queryString?.let {
            size += it.length
        }
        
        // Content length
        if (request.contentLength > 0) {
            size += request.contentLength
        }
        
        return size
    }

    /**
     * 응답 크기를 대략적으로 계산합니다.
     */
    private fun calculateResponseSize(response: HttpServletResponse): Long {
        var size = 0L
        
        // Headers
        response.headerNames.forEach { headerName ->
            val headerValue = response.getHeader(headerName)
            size += headerName.length + (headerValue?.length ?: 0)
        }
        
        // Content length가 설정되어 있다면 사용
        val contentLength = response.getHeader("Content-Length")
        contentLength?.toLongOrNull()?.let {
            size += it
        }
        
        return size
    }
}