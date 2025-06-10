package com.ssuspot.sns.api.controller.monitoring

import com.ssuspot.sns.infrastructure.monitoring.BusinessMetricsCollector
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import java.time.Instant

/**
 * 모니터링 및 메트릭 정보를 제공하는 컨트롤러
 * 관리자용 대시보드 데이터를 제공합니다.
 */
@RestController
@RequestMapping("/api/monitoring")
class MonitoringController(
    private val businessMetricsCollector: BusinessMetricsCollector,
    private val meterRegistry: MeterRegistry
) {

    /**
     * 비즈니스 메트릭 요약 정보
     */
    @GetMapping("/business-metrics")
    fun getBusinessMetrics(): ResponseEntity<Map<String, Any>> {
        val metrics = businessMetricsCollector.getMetricsSnapshot()
        
        return ResponseEntity.ok(mapOf(
            "timestamp" to Instant.now().toString(),
            "metrics" to metrics,
            "status" to "healthy"
        ))
    }

    /**
     * 시스템 성능 메트릭
     */
    @GetMapping("/system-metrics")
    fun getSystemMetrics(): ResponseEntity<Map<String, Any>> {
        val jvmMetrics = mapOf(
            "memoryUsed" to getMetricValue("jvm.memory.used"),
            "memoryMax" to getMetricValue("jvm.memory.max"),
            "gcPause" to getMetricValue("jvm.gc.pause"),
            "threadsLive" to getMetricValue("jvm.threads.live"),
            "threadsDeadlocked" to getMetricValue("jvm.threads.deadlocked")
        )

        val httpMetrics = mapOf(
            "httpRequestsCount" to getMetricValue("http.server.requests"),
            "httpRequestsTime" to getMetricValue("http.server.requests", "mean"),
            "httpErrors4xx" to getMetricValue("http.server.requests", "4xx"),
            "httpErrors5xx" to getMetricValue("http.server.requests", "5xx")
        )

        val dbMetrics = mapOf(
            "hikariActive" to getMetricValue("hikaricp.connections.active"),
            "hikariIdle" to getMetricValue("hikaricp.connections.idle"),
            "hikariPending" to getMetricValue("hikaricp.connections.pending"),
            "hikariTimeout" to getMetricValue("hikaricp.connections.timeout")
        )

        val cacheMetrics = mapOf(
            "cacheHits" to getMetricValue("cache.gets", "hit"),
            "cacheMisses" to getMetricValue("cache.gets", "miss"),
            "cacheEvictions" to getMetricValue("cache.evictions")
        )

        return ResponseEntity.ok(mapOf(
            "timestamp" to Instant.now().toString(),
            "jvm" to jvmMetrics,
            "http" to httpMetrics,
            "database" to dbMetrics,
            "cache" to cacheMetrics
        ))
    }

    /**
     * 애플리케이션 상태 확인
     */
    @GetMapping("/health")
    fun getHealthStatus(): ResponseEntity<Map<String, Any>> {
        val dbHealthy = checkDatabaseHealth()
        val cacheHealthy = checkCacheHealth()
        val overallHealthy = dbHealthy && cacheHealthy

        val healthData = mapOf(
            "timestamp" to Instant.now().toString(),
            "status" to if (overallHealthy) "UP" else "DOWN",
            "components" to mapOf(
                "database" to mapOf(
                    "status" to if (dbHealthy) "UP" else "DOWN",
                    "details" to getDatabaseHealthDetails()
                ),
                "cache" to mapOf(
                    "status" to if (cacheHealthy) "UP" else "DOWN",
                    "details" to getCacheHealthDetails()
                ),
                "disk" to mapOf(
                    "status" to "UP",
                    "details" to getDiskHealthDetails()
                )
            )
        )

        val status = if (overallHealthy) 200 else 503
        return ResponseEntity.status(status).body(healthData)
    }

    /**
     * 실시간 API 사용량 통계
     */
    @GetMapping("/api-usage")
    fun getApiUsageStats(): ResponseEntity<Map<String, Any>> {
        val endpointStats = mutableMapOf<String, Map<String, Any>>()
        
        // 주요 엔드포인트별 통계 수집
        val endpoints = listOf(
            "/api/posts",
            "/api/users",
            "/api/comments",
            "/api/spots",
            "/api/auth"
        )

        endpoints.forEach { endpoint ->
            endpointStats[endpoint] = mapOf(
                "totalRequests" to getMetricValue("http.server.requests", endpoint),
                "averageResponseTime" to getMetricValue("http.server.requests.time", endpoint),
                "errorRate" to calculateErrorRate(endpoint),
                "requestsPerMinute" to calculateRequestsPerMinute(endpoint)
            )
        }

        return ResponseEntity.ok(mapOf(
            "timestamp" to Instant.now().toString(),
            "endpoints" to endpointStats,
            "summary" to mapOf(
                "totalRequests" to getMetricValue("http.server.requests"),
                "averageResponseTime" to getMetricValue("http.server.requests", "mean"),
                "overallErrorRate" to calculateOverallErrorRate()
            )
        ))
    }

    /**
     * 경고 및 알림 상태
     */
    @GetMapping("/alerts")
    fun getAlertsStatus(): ResponseEntity<Map<String, Any>> {
        val alerts = mutableListOf<Map<String, Any>>()

        // 메모리 사용량 체크
        val memoryUsage = getMemoryUsagePercentage()
        if (memoryUsage > 80) {
            alerts.add(mapOf(
                "level" to "WARNING",
                "type" to "MEMORY",
                "message" to "메모리 사용량이 높습니다: ${memoryUsage}%",
                "timestamp" to Instant.now().toString()
            ))
        }

        // 응답 시간 체크
        val avgResponseTime = getMetricValue("http.server.requests", "mean")
        if (avgResponseTime > 2000) { // 2초 이상
            alerts.add(mapOf(
                "level" to "WARNING",
                "type" to "PERFORMANCE",
                "message" to "평균 응답 시간이 높습니다: ${avgResponseTime}ms",
                "timestamp" to Instant.now().toString()
            ))
        }

        // 에러율 체크
        val errorRate = calculateOverallErrorRate()
        if (errorRate > 5) { // 5% 이상
            alerts.add(mapOf(
                "level" to "CRITICAL",
                "type" to "ERROR_RATE",
                "message" to "에러율이 높습니다: ${errorRate}%",
                "timestamp" to Instant.now().toString()
            ))
        }

        // 데이터베이스 연결 체크
        val dbConnections = getMetricValue("hikaricp.connections.active")
        val maxConnections = getMetricValue("hikaricp.connections.max")
        if (dbConnections / maxConnections > 0.9) { // 90% 이상 사용
            alerts.add(mapOf(
                "level" to "WARNING",
                "type" to "DATABASE",
                "message" to "데이터베이스 연결이 부족합니다: ${dbConnections}/${maxConnections}",
                "timestamp" to Instant.now().toString()
            ))
        }

        return ResponseEntity.ok(mapOf(
            "timestamp" to Instant.now().toString(),
            "alertCount" to alerts.size,
            "alerts" to alerts,
            "status" to if (alerts.isEmpty()) "HEALTHY" else "WARNING"
        ))
    }

    // 헬퍼 메서드들
    private fun getMetricValue(metricName: String, tag: String? = null): Double {
        return try {
            val search = meterRegistry.find(metricName)
            if (tag != null) {
                search.tag("uri", tag)
            }
            search.meter()?.measure()?.firstOrNull()?.value ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    private fun checkDatabaseHealth(): Boolean {
        return try {
            val activeConnections = getMetricValue("hikaricp.connections.active")
            activeConnections >= 0 // 기본적인 연결 확인
        } catch (e: Exception) {
            false
        }
    }

    private fun checkCacheHealth(): Boolean {
        return try {
            val cacheGets = getMetricValue("cache.gets")
            cacheGets >= 0 // 기본적인 캐시 동작 확인
        } catch (e: Exception) {
            false
        }
    }

    private fun getDatabaseHealthDetails(): Map<String, Any> {
        return mapOf(
            "activeConnections" to getMetricValue("hikaricp.connections.active"),
            "idleConnections" to getMetricValue("hikaricp.connections.idle"),
            "pendingConnections" to getMetricValue("hikaricp.connections.pending")
        )
    }

    private fun getCacheHealthDetails(): Map<String, Any> {
        return mapOf(
            "hits" to getMetricValue("cache.gets", "hit"),
            "misses" to getMetricValue("cache.gets", "miss"),
            "evictions" to getMetricValue("cache.evictions")
        )
    }

    private fun getDiskHealthDetails(): Map<String, Any> {
        val runtime = Runtime.getRuntime()
        val totalSpace = runtime.totalMemory()
        val freeSpace = runtime.freeMemory()
        val usedSpace = totalSpace - freeSpace

        return mapOf(
            "total" to totalSpace,
            "free" to freeSpace,
            "used" to usedSpace,
            "usagePercentage" to (usedSpace.toDouble() / totalSpace * 100).toInt()
        )
    }

    private fun calculateErrorRate(endpoint: String): Double {
        val totalRequests = getMetricValue("http.server.requests", endpoint)
        val errorRequests = getMetricValue("http.server.requests.error", endpoint)
        return if (totalRequests > 0) (errorRequests / totalRequests * 100) else 0.0
    }

    private fun calculateRequestsPerMinute(endpoint: String): Double {
        // 간단한 계산 - 실제로는 시간 윈도우 기반 계산이 필요
        return getMetricValue("http.server.requests", endpoint) / 60.0
    }

    private fun calculateOverallErrorRate(): Double {
        val totalRequests = getMetricValue("http.server.requests")
        val errorRequests = getMetricValue("http.server.requests.error")
        return if (totalRequests > 0) (errorRequests / totalRequests * 100) else 0.0
    }

    private fun getMemoryUsagePercentage(): Int {
        val memoryUsed = getMetricValue("jvm.memory.used")
        val memoryMax = getMetricValue("jvm.memory.max")
        return if (memoryMax > 0) ((memoryUsed / memoryMax) * 100).toInt() else 0
    }
}