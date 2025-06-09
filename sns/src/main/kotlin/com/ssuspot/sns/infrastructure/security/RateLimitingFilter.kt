package com.ssuspot.sns.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.api.response.common.ErrorResponse
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

/**
 * Redis 기반 API Rate Limiting Filter
 * IP 주소별로 요청 횟수를 제한하여 API 남용을 방지
 */
@Component
class RateLimitingFilter(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : Filter {

    companion object {
        // Rate Limit 설정
        private const val MAX_REQUESTS_PER_MINUTE = 60
        private const val MAX_REQUESTS_PER_HOUR = 1000
        private const val WINDOW_SIZE_MINUTES = 1
        private const val WINDOW_SIZE_HOURS = 60
        
        // Redis 키 prefix
        private const val RATE_LIMIT_PREFIX = "rate_limit"
        private const val MINUTE_PREFIX = "minute"
        private const val HOUR_PREFIX = "hour"
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        
        // 정적 리소스와 health check는 제외
        if (shouldSkipRateLimit(httpRequest)) {
            chain.doFilter(request, response)
            return
        }
        
        val clientIp = getClientIpAddress(httpRequest)
        
        try {
            // 분당 및 시간당 요청 수 확인
            if (isRateLimitExceeded(clientIp)) {
                sendRateLimitError(httpResponse)
                return
            }
            
            // 요청 수 증가
            incrementRequestCount(clientIp)
            
            // 응답 헤더에 Rate Limit 정보 추가
            addRateLimitHeaders(httpResponse, clientIp)
            
            chain.doFilter(request, response)
            
        } catch (e: Exception) {
            // Redis 오류 시에는 Rate Limiting을 우회
            chain.doFilter(request, response)
        }
    }
    
    private fun shouldSkipRateLimit(request: HttpServletRequest): Boolean {
        val uri = request.requestURI
        val skipPaths = listOf(
            "/actuator",
            "/health",
            "/css",
            "/js",
            "/images",
            "/favicon.ico"
        )
        return skipPaths.any { uri.startsWith(it) }
    }
    
    private fun getClientIpAddress(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        val xRealIp = request.getHeader("X-Real-IP")
        
        return when {
            !xForwardedFor.isNullOrBlank() -> xForwardedFor.split(",")[0].trim()
            !xRealIp.isNullOrBlank() -> xRealIp
            else -> request.remoteAddr
        }
    }
    
    private fun isRateLimitExceeded(clientIp: String): Boolean {
        val minuteCount = getCurrentRequestCount(clientIp, MINUTE_PREFIX)
        val hourCount = getCurrentRequestCount(clientIp, HOUR_PREFIX)
        
        return minuteCount >= MAX_REQUESTS_PER_MINUTE || hourCount >= MAX_REQUESTS_PER_HOUR
    }
    
    private fun getCurrentRequestCount(clientIp: String, timeWindow: String): Int {
        val key = generateRedisKey(clientIp, timeWindow)
        val count = redisTemplate.opsForValue().get(key)
        return count?.toIntOrNull() ?: 0
    }
    
    private fun incrementRequestCount(clientIp: String) {
        val now = Instant.now()
        
        // 분당 카운터
        val minuteKey = generateRedisKey(clientIp, MINUTE_PREFIX)
        val minuteCount = redisTemplate.opsForValue().increment(minuteKey) ?: 1
        if (minuteCount == 1L) {
            redisTemplate.expire(minuteKey, Duration.ofMinutes(WINDOW_SIZE_MINUTES.toLong()))
        }
        
        // 시간당 카운터
        val hourKey = generateRedisKey(clientIp, HOUR_PREFIX)
        val hourCount = redisTemplate.opsForValue().increment(hourKey) ?: 1
        if (hourCount == 1L) {
            redisTemplate.expire(hourKey, Duration.ofMinutes(WINDOW_SIZE_HOURS.toLong()))
        }
    }
    
    private fun generateRedisKey(clientIp: String, timeWindow: String): String {
        val now = Instant.now()
        return when (timeWindow) {
            MINUTE_PREFIX -> {
                val minute = now.epochSecond / 60
                "$RATE_LIMIT_PREFIX:$timeWindow:$clientIp:$minute"
            }
            HOUR_PREFIX -> {
                val hour = now.epochSecond / 3600
                "$RATE_LIMIT_PREFIX:$timeWindow:$clientIp:$hour"
            }
            else -> "$RATE_LIMIT_PREFIX:$timeWindow:$clientIp"
        }
    }
    
    private fun addRateLimitHeaders(response: HttpServletResponse, clientIp: String) {
        val minuteCount = getCurrentRequestCount(clientIp, MINUTE_PREFIX)
        val hourCount = getCurrentRequestCount(clientIp, HOUR_PREFIX)
        
        response.setHeader("X-RateLimit-Minute-Limit", MAX_REQUESTS_PER_MINUTE.toString())
        response.setHeader("X-RateLimit-Minute-Remaining", (MAX_REQUESTS_PER_MINUTE - minuteCount).coerceAtLeast(0).toString())
        response.setHeader("X-RateLimit-Hour-Limit", MAX_REQUESTS_PER_HOUR.toString())
        response.setHeader("X-RateLimit-Hour-Remaining", (MAX_REQUESTS_PER_HOUR - hourCount).coerceAtLeast(0).toString())
    }
    
    private fun sendRateLimitError(response: HttpServletResponse) {
        response.status = HttpStatus.TOO_MANY_REQUESTS.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.TOO_MANY_REQUESTS,
            errCode = "TOO_MANY_REQUESTS",
            message = "API 요청 한도를 초과했습니다. 잠시 후 다시 시도해 주세요."
        )
        
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
        response.writer.flush()
    }
}