package com.ssuspot.sns.infrastructure.monitoring

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap

/**
 * 로그 집계 및 분석 서비스
 * 로그 파일을 분석하여 통계 정보를 생성합니다.
 */
@Service
class LogAnalyticsService {

    companion object {
        private val logger = LoggerFactory.getLogger(LogAnalyticsService::class.java)
        private const val LOG_PATH = "./logs"
    }

    // 일일 통계 데이터
    private val dailyStats = ConcurrentHashMap<String, MutableMap<String, Long>>()
    
    // 시간별 통계 데이터
    private val hourlyStats = ConcurrentHashMap<String, MutableMap<String, Long>>()

    /**
     * 매시간 로그 분석 실행 (정시마다)
     */
    @Scheduled(cron = "0 0 * * * *") // 매시간 정시
    fun analyzeHourlyLogs() {
        try {
            logger.info("Starting hourly log analysis")
            
            val currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"))
            
            analyzeApplicationLogs(currentHour)
            analyzeSecurityLogs(currentHour)
            analyzePerformanceLogs(currentHour)
            analyzeBusinessLogs(currentHour)
            
            logger.info("Hourly log analysis completed for: {}", currentHour)
        } catch (e: Exception) {
            logger.error("Error during hourly log analysis", e)
        }
    }

    /**
     * 매일 자정 일일 통계 생성
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    fun generateDailyStatistics() {
        try {
            logger.info("Starting daily statistics generation")
            
            val yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            
            generateDailyUserStatistics(yesterday)
            generateDailyContentStatistics(yesterday)
            generateDailySystemStatistics(yesterday)
            generateDailySecurityStatistics(yesterday)
            
            logger.info("Daily statistics generation completed for: {}", yesterday)
        } catch (e: Exception) {
            logger.error("Error during daily statistics generation", e)
        }
    }

    /**
     * 애플리케이션 로그 분석
     */
    private fun analyzeApplicationLogs(timeKey: String) {
        try {
            val logFile = Paths.get("$LOG_PATH/application.log")
            if (!Files.exists(logFile)) {
                return
            }

            val stats = mutableMapOf<String, Long>()
            
            Files.lines(logFile).use { lines ->
                lines.forEach { line ->
                    // 로그 레벨별 카운트
                    when {
                        line.contains("ERROR") -> stats.merge("errors", 1, Long::plus)
                        line.contains("WARN") -> stats.merge("warnings", 1, Long::plus)
                        line.contains("INFO") -> stats.merge("info", 1, Long::plus)
                        line.contains("DEBUG") -> stats.merge("debug", 1, Long::plus)
                    }
                    
                    // HTTP 요청 패턴 분석
                    if (line.contains("HTTP")) {
                        stats.merge("httpRequests", 1, Long::plus)
                        
                        when {
                            line.contains("POST") -> stats.merge("postRequests", 1, Long::plus)
                            line.contains("GET") -> stats.merge("getRequests", 1, Long::plus)
                            line.contains("PUT") -> stats.merge("putRequests", 1, Long::plus)
                            line.contains("DELETE") -> stats.merge("deleteRequests", 1, Long::plus)
                        }
                    }
                }
            }
            
            hourlyStats[timeKey] = stats
            logger.debug("Application log analysis completed: {} entries processed", stats.values.sum())
            
        } catch (e: Exception) {
            logger.warn("Failed to analyze application logs for {}: {}", timeKey, e.message)
        }
    }

    /**
     * 보안 로그 분석
     */
    private fun analyzeSecurityLogs(timeKey: String) {
        try {
            val logFile = Paths.get("$LOG_PATH/security.log")
            if (!Files.exists(logFile)) {
                return
            }

            val stats = mutableMapOf<String, Long>()
            
            Files.lines(logFile).use { lines ->
                lines.forEach { line ->
                    // 보안 이벤트별 카운트
                    when {
                        line.contains("LOGIN_SUCCESS") -> stats.merge("successfulLogins", 1, Long::plus)
                        line.contains("LOGIN_FAILURE") -> stats.merge("failedLogins", 1, Long::plus)
                        line.contains("UNAUTHORIZED_ACCESS") -> stats.merge("unauthorizedAccess", 1, Long::plus)
                        line.contains("RATE_LIMIT_EXCEEDED") -> stats.merge("rateLimitExceeded", 1, Long::plus)
                        line.contains("SUSPICIOUS_ACTIVITY") -> stats.merge("suspiciousActivity", 1, Long::plus)
                        line.contains("XSS_ATTEMPT") -> stats.merge("xssAttempts", 1, Long::plus)
                        line.contains("SQL_INJECTION_ATTEMPT") -> stats.merge("sqlInjectionAttempts", 1, Long::plus)
                    }
                }
            }
            
            // 보안 통계를 별도로 저장
            hourlyStats["$timeKey-security"] = stats
            logger.debug("Security log analysis completed: {} security events", stats.values.sum())
            
        } catch (e: Exception) {
            logger.warn("Failed to analyze security logs for {}: {}", timeKey, e.message)
        }
    }

    /**
     * 성능 로그 분석
     */
    private fun analyzePerformanceLogs(timeKey: String) {
        try {
            val logFile = Paths.get("$LOG_PATH/performance.log")
            if (!Files.exists(logFile)) {
                return
            }

            val stats = mutableMapOf<String, Long>()
            var totalResponseTime = 0L
            var responseTimeCount = 0L
            
            Files.lines(logFile).use { lines ->
                lines.forEach { line ->
                    // JSON 파싱하여 성능 데이터 추출 (간단한 예시)
                    if (line.contains("\"eventType\":\"API_CALL\"")) {
                        stats.merge("apiCalls", 1, Long::plus)
                        
                        // 응답 시간 추출 (정규식 사용)
                        val durationMatch = Regex("\"duration\":(\\d+)").find(line)
                        if (durationMatch != null) {
                            val duration = durationMatch.groupValues[1].toLongOrNull() ?: 0
                            totalResponseTime += duration
                            responseTimeCount++
                            
                            // 응답 시간 구간별 분류
                            when {
                                duration < 100 -> stats.merge("fastResponses", 1, Long::plus)
                                duration < 500 -> stats.merge("mediumResponses", 1, Long::plus)
                                duration < 1000 -> stats.merge("slowResponses", 1, Long::plus)
                                else -> stats.merge("verySlowResponses", 1, Long::plus)
                            }
                        }
                        
                        // 상태 코드별 분류
                        when {
                            line.contains("\"statusCode\":200") -> stats.merge("status200", 1, Long::plus)
                            line.contains("\"statusCode\":4") -> stats.merge("status4xx", 1, Long::plus)
                            line.contains("\"statusCode\":5") -> stats.merge("status5xx", 1, Long::plus)
                        }
                    }
                    
                    if (line.contains("\"eventType\":\"DATABASE\"")) {
                        stats.merge("databaseOperations", 1, Long::plus)
                    }
                }
            }
            
            // 평균 응답 시간 계산
            if (responseTimeCount > 0) {
                stats["averageResponseTime"] = totalResponseTime / responseTimeCount
            }
            
            hourlyStats["$timeKey-performance"] = stats
            logger.debug("Performance log analysis completed: {} performance metrics", stats.size)
            
        } catch (e: Exception) {
            logger.warn("Failed to analyze performance logs for {}: {}", timeKey, e.message)
        }
    }

    /**
     * 비즈니스 로그 분석
     */
    private fun analyzeBusinessLogs(timeKey: String) {
        try {
            val logFile = Paths.get("$LOG_PATH/business.log")
            if (!Files.exists(logFile)) {
                return
            }

            val stats = mutableMapOf<String, Long>()
            
            Files.lines(logFile).use { lines ->
                lines.forEach { line ->
                    // 비즈니스 액션별 카운트
                    when {
                        line.contains("\"action\":\"CREATE\"") -> {
                            stats.merge("createActions", 1, Long::plus)
                            when {
                                line.contains("\"entityType\":\"POST\"") -> stats.merge("postsCreated", 1, Long::plus)
                                line.contains("\"entityType\":\"USER\"") -> stats.merge("usersRegistered", 1, Long::plus)
                                line.contains("\"entityType\":\"COMMENT\"") -> stats.merge("commentsCreated", 1, Long::plus)
                            }
                        }
                        line.contains("\"action\":\"LIKE\"") -> stats.merge("likesGiven", 1, Long::plus)
                        line.contains("\"action\":\"FOLLOW\"") -> stats.merge("followActions", 1, Long::plus)
                        line.contains("\"action\":\"VIEW\"") -> stats.merge("pageViews", 1, Long::plus)
                        line.contains("\"action\":\"SEARCH\"") -> stats.merge("searches", 1, Long::plus)
                        line.contains("\"action\":\"LOGIN\"") -> stats.merge("logins", 1, Long::plus)
                    }
                }
            }
            
            hourlyStats["$timeKey-business"] = stats
            logger.debug("Business log analysis completed: {} business actions", stats.values.sum())
            
        } catch (e: Exception) {
            logger.warn("Failed to analyze business logs for {}: {}", timeKey, e.message)
        }
    }

    /**
     * 일일 사용자 통계 생성
     */
    private fun generateDailyUserStatistics(date: String) {
        val userStats = mutableMapOf<String, Long>()
        
        // 시간별 데이터를 집계하여 일일 통계 생성
        hourlyStats.entries.filter { it.key.startsWith(date) && it.key.contains("business") }
            .forEach { (_, stats) ->
                stats.forEach { (key, value) ->
                    userStats.merge(key, value, Long::plus)
                }
            }
        
        dailyStats[date] = userStats
        logger.info("Daily user statistics generated for {}: {}", date, userStats)
    }

    /**
     * 일일 콘텐츠 통계 생성
     */
    private fun generateDailyContentStatistics(date: String) {
        // 콘텐츠 관련 통계 집계
        logger.debug("Generating daily content statistics for {}", date)
    }

    /**
     * 일일 시스템 통계 생성
     */
    private fun generateDailySystemStatistics(date: String) {
        // 시스템 성능 통계 집계
        logger.debug("Generating daily system statistics for {}", date)
    }

    /**
     * 일일 보안 통계 생성
     */
    private fun generateDailySecurityStatistics(date: String) {
        // 보안 이벤트 통계 집계
        logger.debug("Generating daily security statistics for {}", date)
    }

    /**
     * 통계 데이터 조회 메서드들
     */
    fun getHourlyStats(timeKey: String): Map<String, Long>? {
        return hourlyStats[timeKey]?.toMap()
    }

    fun getDailyStats(date: String): Map<String, Long>? {
        return dailyStats[date]?.toMap()
    }

    fun getAllDailyStats(): Map<String, Map<String, Long>> {
        return dailyStats.mapValues { it.value.toMap() }
    }

    /**
     * 오래된 통계 데이터 정리 (주간 실행)
     */
    @Scheduled(cron = "0 0 0 * * SUN") // 매주 일요일 자정
    fun cleanupOldStatistics() {
        try {
            val cutoffDate = LocalDateTime.now().minusDays(30)
            val cutoffKey = cutoffDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            
            // 30일 이전 데이터 삭제
            dailyStats.keys.removeIf { it < cutoffKey }
            hourlyStats.keys.removeIf { it < cutoffKey }
            
            logger.info("Cleaned up statistics older than {}", cutoffKey)
        } catch (e: Exception) {
            logger.error("Error during statistics cleanup", e)
        }
    }
}