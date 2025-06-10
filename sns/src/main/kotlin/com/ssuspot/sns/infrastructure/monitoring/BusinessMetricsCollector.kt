package com.ssuspot.sns.infrastructure.monitoring

import com.ssuspot.sns.infrastructure.logging.StructuredLoggerFactory
import com.ssuspot.sns.infrastructure.logging.BusinessAction
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 비즈니스 메트릭 수집을 담당하는 클래스
 * 사용자 활동, 게시물 상호작용, 시스템 사용량 등을 추적합니다.
 */
@Component
class BusinessMetricsCollector(
    private val meterRegistry: MeterRegistry,
    private val loggerFactory: StructuredLoggerFactory
) {

    // 실시간 메트릭을 위한 카운터들
    private val activeUsers = AtomicLong(0)
    private val dailyActiveUsers = ConcurrentHashMap<String, AtomicLong>()
    private val userSessions = ConcurrentHashMap<String, LocalDateTime>()

    // 메트릭 카운터들
    private val userRegistrationCounter: Counter
    private val userLoginCounter: Counter
    private val postCreationCounter: Counter
    private val postLikeCounter: Counter
    private val commentCreationCounter: Counter
    private val followActionCounter: Counter
    private val spotCreationCounter: Counter
    private val imageUploadCounter: Counter

    // 타이머들
    private val postCreationTimer: Timer
    private val userLoginTimer: Timer
    private val imageUploadTimer: Timer

    init {
        // 카운터 초기화
        userRegistrationCounter = Counter.builder("business.user.registrations")
            .description("사용자 등록 수")
            .register(meterRegistry)

        userLoginCounter = Counter.builder("business.user.logins")
            .description("사용자 로그인 수")
            .register(meterRegistry)

        postCreationCounter = Counter.builder("business.post.creations")
            .description("게시물 생성 수")
            .register(meterRegistry)

        postLikeCounter = Counter.builder("business.post.likes")
            .description("게시물 좋아요 수")
            .register(meterRegistry)

        commentCreationCounter = Counter.builder("business.comment.creations")
            .description("댓글 생성 수")
            .register(meterRegistry)

        followActionCounter = Counter.builder("business.user.follows")
            .description("팔로우 액션 수")
            .register(meterRegistry)

        spotCreationCounter = Counter.builder("business.spot.creations")
            .description("스팟 생성 수")
            .register(meterRegistry)

        imageUploadCounter = Counter.builder("business.image.uploads")
            .description("이미지 업로드 수")
            .register(meterRegistry)

        // 타이머 초기화
        postCreationTimer = Timer.builder("business.post.creation.time")
            .description("게시물 생성 소요 시간")
            .register(meterRegistry)

        userLoginTimer = Timer.builder("business.user.login.time")
            .description("사용자 로그인 소요 시간")
            .register(meterRegistry)

        imageUploadTimer = Timer.builder("business.image.upload.time")
            .description("이미지 업로드 소요 시간")
            .register(meterRegistry)

        // 게이지 초기화
        Gauge.builder("business.users.active")
            .description("현재 활성 사용자 수")
            .register(meterRegistry) { activeUsers.get().toDouble() }

        Gauge.builder("business.users.sessions")
            .description("현재 활성 세션 수")
            .register(meterRegistry) { userSessions.size.toDouble() }
    }

    /**
     * 사용자 등록 메트릭
     */
    fun recordUserRegistration(userId: String, email: String) {
        userRegistrationCounter.increment()
        
        loggerFactory.logBusiness(
            action = BusinessAction.REGISTER,
            entityType = "USER",
            entityId = userId,
            userId = userId,
            details = mapOf(
                "email" to email,
                "registrationTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 사용자 로그인 메트릭
     */
    fun recordUserLogin(userId: String, duration: Long) {
        userLoginCounter.increment()
        userLoginTimer.record(duration, java.util.concurrent.TimeUnit.MILLISECONDS)
        
        // 활성 사용자 및 세션 관리
        activeUsers.incrementAndGet()
        userSessions[userId] = LocalDateTime.now()
        
        // 일일 활성 사용자 추가
        val today = LocalDateTime.now().toLocalDate().toString()
        dailyActiveUsers.computeIfAbsent(today) { AtomicLong(0) }.incrementAndGet()
        
        loggerFactory.logBusiness(
            action = BusinessAction.LOGIN,
            entityType = "USER",
            entityId = userId,
            userId = userId,
            details = mapOf(
                "loginDuration" to duration,
                "loginTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 사용자 로그아웃 메트릭
     */
    fun recordUserLogout(userId: String) {
        activeUsers.decrementAndGet()
        userSessions.remove(userId)
        
        loggerFactory.logBusiness(
            action = BusinessAction.LOGOUT,
            entityType = "USER",
            entityId = userId,
            userId = userId,
            details = mapOf(
                "logoutTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 게시물 생성 메트릭
     */
    fun recordPostCreation(postId: String, userId: String, spotId: String, duration: Long) {
        postCreationCounter.increment()
        postCreationTimer.record(duration, java.util.concurrent.TimeUnit.MILLISECONDS)
        
        loggerFactory.logBusiness(
            action = BusinessAction.CREATE,
            entityType = "POST",
            entityId = postId,
            userId = userId,
            details = mapOf(
                "spotId" to spotId,
                "creationDuration" to duration,
                "creationTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 게시물 좋아요 메트릭
     */
    fun recordPostLike(postId: String, userId: String, isLike: Boolean) {
        if (isLike) {
            postLikeCounter.increment()
        }
        
        loggerFactory.logBusiness(
            action = if (isLike) BusinessAction.LIKE else BusinessAction.UNLIKE,
            entityType = "POST",
            entityId = postId,
            userId = userId,
            details = mapOf(
                "action" to if (isLike) "LIKE" else "UNLIKE",
                "timestamp" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 댓글 생성 메트릭
     */
    fun recordCommentCreation(commentId: String, postId: String, userId: String) {
        commentCreationCounter.increment()
        
        loggerFactory.logBusiness(
            action = BusinessAction.COMMENT,
            entityType = "COMMENT",
            entityId = commentId,
            userId = userId,
            details = mapOf(
                "postId" to postId,
                "creationTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 팔로우 액션 메트릭
     */
    fun recordFollowAction(followingUserId: String, followedUserId: String, isFollow: Boolean) {
        followActionCounter.increment()
        
        loggerFactory.logBusiness(
            action = if (isFollow) BusinessAction.FOLLOW else BusinessAction.UNFOLLOW,
            entityType = "USER",
            entityId = followedUserId,
            userId = followingUserId,
            details = mapOf(
                "action" to if (isFollow) "FOLLOW" else "UNFOLLOW",
                "targetUserId" to followedUserId,
                "timestamp" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 스팟 생성 메트릭
     */
    fun recordSpotCreation(spotId: String, creatorId: String?) {
        spotCreationCounter.increment()
        
        loggerFactory.logBusiness(
            action = BusinessAction.CREATE,
            entityType = "SPOT",
            entityId = spotId,
            userId = creatorId,
            details = mapOf(
                "creationTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 이미지 업로드 메트릭
     */
    fun recordImageUpload(imageId: String, userId: String, fileSize: Long, duration: Long) {
        imageUploadCounter.increment()
        imageUploadTimer.record(duration, java.util.concurrent.TimeUnit.MILLISECONDS)
        
        loggerFactory.logBusiness(
            action = BusinessAction.UPLOAD,
            entityType = "IMAGE",
            entityId = imageId,
            userId = userId,
            details = mapOf(
                "fileSize" to fileSize,
                "uploadDuration" to duration,
                "uploadTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 게시물 조회 메트릭
     */
    fun recordPostView(postId: String, userId: String?) {
        loggerFactory.logBusiness(
            action = BusinessAction.VIEW,
            entityType = "POST",
            entityId = postId,
            userId = userId,
            details = mapOf(
                "viewTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 검색 메트릭
     */
    fun recordSearch(searchQuery: String, userId: String?, resultCount: Int) {
        loggerFactory.logBusiness(
            action = BusinessAction.SEARCH,
            entityType = "SEARCH",
            userId = userId,
            details = mapOf(
                "query" to searchQuery,
                "resultCount" to resultCount,
                "searchTime" to LocalDateTime.now().toString()
            )
        )
    }

    /**
     * 세션 정리 (주기적으로 호출되어야 함)
     */
    fun cleanupInactiveSessions() {
        val cutoffTime = LocalDateTime.now().minusHours(1)
        val inactiveSessions = userSessions.filter { it.value.isBefore(cutoffTime) }
        
        inactiveSessions.forEach { (userId, _) ->
            userSessions.remove(userId)
            activeUsers.decrementAndGet()
        }
    }

    /**
     * 일일 활성 사용자 정리 (일일 배치로 호출)
     */
    fun cleanupOldDailyMetrics() {
        val cutoffDate = LocalDateTime.now().minusDays(30).toLocalDate().toString()
        dailyActiveUsers.keys.removeIf { it < cutoffDate }
    }

    /**
     * 현재 메트릭 스냅샷 반환
     */
    fun getMetricsSnapshot(): Map<String, Any> {
        return mapOf(
            "activeUsers" to activeUsers.get(),
            "activeSessions" to userSessions.size,
            "totalRegistrations" to userRegistrationCounter.count(),
            "totalLogins" to userLoginCounter.count(),
            "totalPosts" to postCreationCounter.count(),
            "totalLikes" to postLikeCounter.count(),
            "totalComments" to commentCreationCounter.count(),
            "totalFollows" to followActionCounter.count(),
            "totalSpots" to spotCreationCounter.count(),
            "totalImageUploads" to imageUploadCounter.count(),
            "averagePostCreationTime" to postCreationTimer.mean(java.util.concurrent.TimeUnit.MILLISECONDS),
            "averageLoginTime" to userLoginTimer.mean(java.util.concurrent.TimeUnit.MILLISECONDS),
            "averageImageUploadTime" to imageUploadTimer.mean(java.util.concurrent.TimeUnit.MILLISECONDS)
        )
    }
}