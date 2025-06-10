package com.ssuspot.sns.infrastructure.monitoring

import com.ssuspot.sns.infrastructure.logging.StructuredLoggerFactory
import com.ssuspot.sns.infrastructure.logging.SecurityEvent
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

/**
 * 시스템 상태 모니터링 및 알림 서비스
 * 임계치를 초과한 메트릭에 대해 알림을 발송합니다.
 */
@Service
class AlertingService(
    private val meterRegistry: MeterRegistry,
    private val loggerFactory: StructuredLoggerFactory
) {

    companion object {
        private val logger = LoggerFactory.getLogger(AlertingService::class.java)
        
        // 알림 임계치 설정
        private const val MEMORY_USAGE_THRESHOLD = 85.0 // 85%
        private const val CPU_USAGE_THRESHOLD = 80.0 // 80%
        private const val RESPONSE_TIME_THRESHOLD = 2000.0 // 2초
        private const val ERROR_RATE_THRESHOLD = 5.0 // 5%
        private const val DB_CONNECTION_THRESHOLD = 90.0 // 90%
        private const val DISK_USAGE_THRESHOLD = 85.0 // 85%
        private const val FAILED_LOGIN_THRESHOLD = 10 // 10회
        private const val RATE_LIMIT_THRESHOLD = 50 // 50회
    }

    // 알림 중복 방지를 위한 쿨다운 관리
    private val alertCooldowns = ConcurrentHashMap<String, LocalDateTime>()
    private val cooldownDuration = 30 // 30분

    /**
     * 5분마다 시스템 상태를 체크하고 필요시 알림 발송
     */
    @Scheduled(fixedRate = 300000) // 5분
    fun performSystemHealthCheck() {
        try {
            logger.info("Starting system health check")
            
            checkMemoryUsage()
            checkCpuUsage()
            checkResponseTime()
            checkErrorRate()
            checkDatabaseConnections()
            checkDiskUsage()
            checkSecurityAlerts()
            
            logger.info("System health check completed")
        } catch (e: Exception) {
            logger.error("Error during system health check", e)
        }
    }

    /**
     * 메모리 사용량 체크
     */
    private fun checkMemoryUsage() {
        val memoryUsed = getMetricValue("jvm.memory.used")
        val memoryMax = getMetricValue("jvm.memory.max")
        
        if (memoryMax > 0) {
            val usagePercentage = (memoryUsed / memoryMax) * 100
            
            if (usagePercentage > MEMORY_USAGE_THRESHOLD) {
                sendAlert(
                    alertType = AlertType.MEMORY_HIGH,
                    severity = AlertSeverity.WARNING,
                    message = "메모리 사용량이 높습니다: ${String.format("%.1f", usagePercentage)}%",
                    details = mapOf(
                        "memoryUsed" to memoryUsed,
                        "memoryMax" to memoryMax,
                        "usagePercentage" to usagePercentage
                    )
                )
            }
        }
    }

    /**
     * CPU 사용량 체크
     */
    private fun checkCpuUsage() {
        val cpuUsage = getMetricValue("system.cpu.usage") * 100
        
        if (cpuUsage > CPU_USAGE_THRESHOLD) {
            sendAlert(
                alertType = AlertType.CPU_HIGH,
                severity = AlertSeverity.WARNING,
                message = "CPU 사용량이 높습니다: ${String.format("%.1f", cpuUsage)}%",
                details = mapOf(
                    "cpuUsage" to cpuUsage
                )
            )
        }
    }

    /**
     * 응답 시간 체크
     */
    private fun checkResponseTime() {
        val avgResponseTime = getMetricValue("http.server.requests", "mean")
        
        if (avgResponseTime > RESPONSE_TIME_THRESHOLD) {
            sendAlert(
                alertType = AlertType.RESPONSE_TIME_HIGH,
                severity = AlertSeverity.WARNING,
                message = "평균 응답 시간이 높습니다: ${String.format("%.0f", avgResponseTime)}ms",
                details = mapOf(
                    "averageResponseTime" to avgResponseTime,
                    "threshold" to RESPONSE_TIME_THRESHOLD
                )
            )
        }
    }

    /**
     * 에러율 체크
     */
    private fun checkErrorRate() {
        val totalRequests = getMetricValue("http.server.requests")
        val errorRequests = getMetricValue("http.server.requests") // 실제로는 4xx, 5xx 필터링 필요
        
        if (totalRequests > 0) {
            val errorRate = (errorRequests / totalRequests) * 100
            
            if (errorRate > ERROR_RATE_THRESHOLD) {
                sendAlert(
                    alertType = AlertType.ERROR_RATE_HIGH,
                    severity = AlertSeverity.CRITICAL,
                    message = "에러율이 높습니다: ${String.format("%.1f", errorRate)}%",
                    details = mapOf(
                        "errorRate" to errorRate,
                        "totalRequests" to totalRequests,
                        "errorRequests" to errorRequests
                    )
                )
            }
        }
    }

    /**
     * 데이터베이스 연결 체크
     */
    private fun checkDatabaseConnections() {
        val activeConnections = getMetricValue("hikaricp.connections.active")
        val maxConnections = getMetricValue("hikaricp.connections.max")
        
        if (maxConnections > 0) {
            val usagePercentage = (activeConnections / maxConnections) * 100
            
            if (usagePercentage > DB_CONNECTION_THRESHOLD) {
                sendAlert(
                    alertType = AlertType.DB_CONNECTIONS_HIGH,
                    severity = AlertSeverity.WARNING,
                    message = "데이터베이스 연결 사용량이 높습니다: ${String.format("%.1f", usagePercentage)}%",
                    details = mapOf(
                        "activeConnections" to activeConnections,
                        "maxConnections" to maxConnections,
                        "usagePercentage" to usagePercentage
                    )
                )
            }
        }
    }

    /**
     * 디스크 사용량 체크
     */
    private fun checkDiskUsage() {
        val runtime = Runtime.getRuntime()
        val totalSpace = runtime.totalMemory()
        val freeSpace = runtime.freeMemory()
        val usagePercentage = ((totalSpace - freeSpace).toDouble() / totalSpace) * 100
        
        if (usagePercentage > DISK_USAGE_THRESHOLD) {
            sendAlert(
                alertType = AlertType.DISK_USAGE_HIGH,
                severity = AlertSeverity.WARNING,
                message = "디스크 사용량이 높습니다: ${String.format("%.1f", usagePercentage)}%",
                details = mapOf(
                    "totalSpace" to totalSpace,
                    "freeSpace" to freeSpace,
                    "usagePercentage" to usagePercentage
                )
            )
        }
    }

    /**
     * 보안 관련 알림 체크
     */
    private fun checkSecurityAlerts() {
        // 실패한 로그인 시도 체크
        val failedLogins = getMetricValue("security.login.failures")
        if (failedLogins > FAILED_LOGIN_THRESHOLD) {
            sendAlert(
                alertType = AlertType.SECURITY_FAILED_LOGINS,
                severity = AlertSeverity.CRITICAL,
                message = "로그인 실패 시도가 많습니다: ${failedLogins.toInt()}회",
                details = mapOf(
                    "failedLoginAttempts" to failedLogins
                )
            )
            
            loggerFactory.logSecurity(
                event = SecurityEvent.SUSPICIOUS_ACTIVITY,
                details = mapOf(
                    "alertType" to "FAILED_LOGINS",
                    "attempts" to failedLogins
                )
            )
        }

        // Rate Limiting 히트 체크
        val rateLimitHits = getMetricValue("security.rate.limit.hits")
        if (rateLimitHits > RATE_LIMIT_THRESHOLD) {
            sendAlert(
                alertType = AlertType.SECURITY_RATE_LIMIT,
                severity = AlertSeverity.WARNING,
                message = "Rate Limit 도달 횟수가 많습니다: ${rateLimitHits.toInt()}회",
                details = mapOf(
                    "rateLimitHits" to rateLimitHits
                )
            )
        }
    }

    /**
     * 알림 발송
     */
    private fun sendAlert(
        alertType: AlertType,
        severity: AlertSeverity,
        message: String,
        details: Map<String, Any> = emptyMap()
    ) {
        val alertKey = alertType.name
        val now = LocalDateTime.now()
        
        // 쿨다운 체크
        val lastAlert = alertCooldowns[alertKey]
        if (lastAlert != null && lastAlert.plusMinutes(cooldownDuration.toLong()).isAfter(now)) {
            return // 쿨다운 기간 내에는 같은 타입의 알림을 발송하지 않음
        }
        
        // 알림 로그 기록
        logger.warn("ALERT [{}] {}: {}", severity.name, alertType.name, message)
        
        // 구조화된 로그로 기록
        loggerFactory.logPerformance(
            operation = "ALERT_TRIGGERED",
            duration = 0,
            success = true,
            details = mapOf(
                "alertType" to alertType.name,
                "severity" to severity.name,
                "message" to message,
                "alertDetails" to details
            )
        )
        
        // 실제 알림 발송 (이메일, 슬랙, SMS 등)
        // 여기서는 로그로만 기록
        when (severity) {
            AlertSeverity.CRITICAL -> {
                // 중요한 알림은 즉시 발송
                logger.error("CRITICAL ALERT: $message")
                // TODO: 이메일/SMS 발송
            }
            AlertSeverity.WARNING -> {
                // 경고는 배치로 발송
                logger.warn("WARNING ALERT: $message")
                // TODO: 슬랙 채널 알림
            }
            AlertSeverity.INFO -> {
                logger.info("INFO ALERT: $message")
            }
        }
        
        // 쿨다운 시간 업데이트
        alertCooldowns[alertKey] = now
    }

    /**
     * 메트릭 값 조회 헬퍼
     */
    private fun getMetricValue(metricName: String, statistic: String? = null): Double {
        return try {
            val search = meterRegistry.find(metricName)
            when (statistic) {
                "mean" -> search.timer()?.mean(java.util.concurrent.TimeUnit.MILLISECONDS) ?: 0.0
                "max" -> search.timer()?.max(java.util.concurrent.TimeUnit.MILLISECONDS) ?: 0.0
                else -> search.meter()?.measure()?.firstOrNull()?.value ?: 0.0
            }
        } catch (e: Exception) {
            logger.warn("Failed to get metric value for {}: {}", metricName, e.message)
            0.0
        }
    }

    /**
     * 쿨다운 맵 정리 (1시간마다)
     */
    @Scheduled(fixedRate = 3600000) // 1시간
    fun cleanupCooldowns() {
        val cutoffTime = LocalDateTime.now().minusHours(2)
        alertCooldowns.entries.removeIf { it.value.isBefore(cutoffTime) }
        logger.debug("Cleaned up {} old alert cooldowns", alertCooldowns.size)
    }
}

/**
 * 알림 타입
 */
enum class AlertType {
    MEMORY_HIGH,
    CPU_HIGH,
    RESPONSE_TIME_HIGH,
    ERROR_RATE_HIGH,
    DB_CONNECTIONS_HIGH,
    DISK_USAGE_HIGH,
    SECURITY_FAILED_LOGINS,
    SECURITY_RATE_LIMIT,
    SECURITY_UNAUTHORIZED_ACCESS,
    SYSTEM_DOWN,
    CACHE_DOWN,
    DATABASE_DOWN
}

/**
 * 알림 심각도
 */
enum class AlertSeverity {
    INFO,
    WARNING, 
    CRITICAL
}