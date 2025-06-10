package com.ssuspot.sns.infrastructure.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

/**
 * 구조화된 로깅을 위한 팩토리 클래스
 * 비즈니스 로직, 보안, 성능 등의 로그를 구분하여 관리합니다.
 */
@Component
class StructuredLoggerFactory {

    companion object {
        private val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        
        // 특수 목적 Logger들
        private val securityLogger: Logger = LoggerFactory.getLogger("SECURITY")
        private val performanceLogger: Logger = LoggerFactory.getLogger("PERFORMANCE")
        private val businessLogger: Logger = LoggerFactory.getLogger("BUSINESS")
        
        // MDC 키 상수들
        const val TRACE_ID = "traceId"
        const val SPAN_ID = "spanId"
        const val USER_ID = "userId"
        const val SESSION_ID = "sessionId"
        const val REQUEST_ID = "requestId"
        const val CLIENT_IP = "clientIp"
        const val USER_AGENT = "userAgent"
        const val REQUEST_URI = "requestUri"
        const val HTTP_METHOD = "httpMethod"
    }

    /**
     * 요청 컨텍스트를 MDC에 설정합니다.
     */
    fun setRequestContext(
        traceId: String = UUID.randomUUID().toString(),
        spanId: String = UUID.randomUUID().toString(),
        userId: String? = null,
        sessionId: String? = null,
        clientIp: String? = null,
        userAgent: String? = null,
        requestUri: String? = null,
        httpMethod: String? = null
    ) {
        MDC.put(TRACE_ID, traceId)
        MDC.put(SPAN_ID, spanId)
        MDC.put(REQUEST_ID, UUID.randomUUID().toString())
        
        userId?.let { MDC.put(USER_ID, it) }
        sessionId?.let { MDC.put(SESSION_ID, it) }
        clientIp?.let { MDC.put(CLIENT_IP, it) }
        userAgent?.let { MDC.put(USER_AGENT, it) }
        requestUri?.let { MDC.put(REQUEST_URI, it) }
        httpMethod?.let { MDC.put(HTTP_METHOD, it) }
    }

    /**
     * MDC 컨텍스트를 정리합니다.
     */
    fun clearContext() {
        MDC.clear()
    }

    /**
     * 보안 관련 로그를 기록합니다.
     */
    fun logSecurity(
        event: SecurityEvent,
        userId: String? = null,
        clientIp: String? = null,
        details: Map<String, Any> = emptyMap()
    ) {
        val logData = mapOf(
            "timestamp" to Instant.now().toString(),
            "event" to event.name,
            "eventType" to "SECURITY",
            "userId" to userId,
            "clientIp" to clientIp,
            "severity" to event.severity.name,
            "details" to details,
            "traceId" to MDC.get(TRACE_ID),
            "requestId" to MDC.get(REQUEST_ID)
        ).filterValues { it != null }

        val message = objectMapper.writeValueAsString(logData)
        
        when (event.severity) {
            SecuritySeverity.HIGH -> securityLogger.error(message)
            SecuritySeverity.MEDIUM -> securityLogger.warn(message)
            SecuritySeverity.LOW -> securityLogger.info(message)
        }
    }

    /**
     * 성능 관련 로그를 기록합니다.
     */
    fun logPerformance(
        operation: String,
        duration: Long,
        success: Boolean = true,
        details: Map<String, Any> = emptyMap()
    ) {
        val logData = mapOf(
            "timestamp" to Instant.now().toString(),
            "eventType" to "PERFORMANCE",
            "operation" to operation,
            "duration" to duration,
            "success" to success,
            "details" to details,
            "traceId" to MDC.get(TRACE_ID),
            "spanId" to MDC.get(SPAN_ID),
            "requestId" to MDC.get(REQUEST_ID),
            "userId" to MDC.get(USER_ID)
        ).filterValues { it != null }

        val message = objectMapper.writeValueAsString(logData)
        
        if (success) {
            performanceLogger.info(message)
        } else {
            performanceLogger.warn(message)
        }
    }

    /**
     * 비즈니스 로직 관련 로그를 기록합니다.
     */
    fun logBusiness(
        action: BusinessAction,
        entityType: String,
        entityId: String? = null,
        userId: String? = null,
        details: Map<String, Any> = emptyMap()
    ) {
        val logData = mapOf(
            "timestamp" to Instant.now().toString(),
            "eventType" to "BUSINESS",
            "action" to action.name,
            "entityType" to entityType,
            "entityId" to entityId,
            "userId" to userId ?: MDC.get(USER_ID),
            "details" to details,
            "traceId" to MDC.get(TRACE_ID),
            "requestId" to MDC.get(REQUEST_ID)
        ).filterValues { it != null }

        val message = objectMapper.writeValueAsString(logData)
        businessLogger.info(message)
    }

    /**
     * API 호출 로그를 기록합니다.
     */
    fun logApiCall(
        endpoint: String,
        method: String,
        statusCode: Int,
        duration: Long,
        userId: String? = null,
        clientIp: String? = null,
        userAgent: String? = null,
        requestSize: Long? = null,
        responseSize: Long? = null
    ) {
        val logData = mapOf(
            "timestamp" to Instant.now().toString(),
            "eventType" to "API_CALL",
            "endpoint" to endpoint,
            "method" to method,
            "statusCode" to statusCode,
            "duration" to duration,
            "userId" to userId ?: MDC.get(USER_ID),
            "clientIp" to clientIp ?: MDC.get(CLIENT_IP),
            "userAgent" to userAgent ?: MDC.get(USER_AGENT),
            "requestSize" to requestSize,
            "responseSize" to responseSize,
            "traceId" to MDC.get(TRACE_ID),
            "requestId" to MDC.get(REQUEST_ID)
        ).filterValues { it != null }

        val message = objectMapper.writeValueAsString(logData)
        
        when {
            statusCode >= 500 -> performanceLogger.error(message)
            statusCode >= 400 -> performanceLogger.warn(message)
            else -> performanceLogger.info(message)
        }
    }

    /**
     * 데이터베이스 작업 로그를 기록합니다.
     */
    fun logDatabaseOperation(
        operation: String,
        table: String,
        duration: Long,
        success: Boolean = true,
        recordCount: Int? = null,
        details: Map<String, Any> = emptyMap()
    ) {
        val logData = mapOf(
            "timestamp" to Instant.now().toString(),
            "eventType" to "DATABASE",
            "operation" to operation,
            "table" to table,
            "duration" to duration,
            "success" to success,
            "recordCount" to recordCount,
            "details" to details,
            "traceId" to MDC.get(TRACE_ID),
            "userId" to MDC.get(USER_ID)
        ).filterValues { it != null }

        val message = objectMapper.writeValueAsString(logData)
        
        if (success) {
            performanceLogger.info(message)
        } else {
            performanceLogger.error(message)
        }
    }
}

/**
 * 보안 이벤트 유형
 */
enum class SecurityEvent(val severity: SecuritySeverity) {
    LOGIN_SUCCESS(SecuritySeverity.LOW),
    LOGIN_FAILURE(SecuritySeverity.MEDIUM),
    LOGOUT(SecuritySeverity.LOW),
    TOKEN_REFRESH(SecuritySeverity.LOW),
    TOKEN_EXPIRED(SecuritySeverity.MEDIUM),
    UNAUTHORIZED_ACCESS(SecuritySeverity.HIGH),
    RATE_LIMIT_EXCEEDED(SecuritySeverity.MEDIUM),
    SUSPICIOUS_ACTIVITY(SecuritySeverity.HIGH),
    PRIVILEGE_ESCALATION(SecuritySeverity.HIGH),
    DATA_ACCESS_VIOLATION(SecuritySeverity.HIGH),
    INPUT_VALIDATION_FAILURE(SecuritySeverity.MEDIUM),
    SQL_INJECTION_ATTEMPT(SecuritySeverity.HIGH),
    XSS_ATTEMPT(SecuritySeverity.HIGH),
    CSRF_ATTEMPT(SecuritySeverity.HIGH),
    ACCOUNT_LOCKED(SecuritySeverity.MEDIUM),
    PASSWORD_CHANGE(SecuritySeverity.LOW),
    USER_REGISTRATION(SecuritySeverity.LOW)
}

/**
 * 보안 이벤트 심각도
 */
enum class SecuritySeverity {
    LOW, MEDIUM, HIGH
}

/**
 * 비즈니스 액션 유형
 */
enum class BusinessAction {
    CREATE,
    READ,
    UPDATE,
    DELETE,
    LOGIN,
    LOGOUT,
    REGISTER,
    FOLLOW,
    UNFOLLOW,
    LIKE,
    UNLIKE,
    COMMENT,
    SHARE,
    UPLOAD,
    DOWNLOAD,
    SEARCH,
    VIEW
}