package com.ssuspot.sns.infrastructure.logging

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

/**
 * 사용자 활동 로깅을 위한 AOP
 * 주요 비즈니스 액션에 대한 사용자 활동을 추적합니다.
 */
@Aspect
@Component
class UserActivityLogger(
    private val loggerFactory: StructuredLoggerFactory
) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserActivityLogger::class.java)
    }

    /**
     * 사용자 관련 활동 로깅
     */
    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.user.UserService.login(..))",
        returning = "result"
    )
    fun logUserLogin(joinPoint: JoinPoint, result: Any) {
        try {
            val args = joinPoint.args
            if (args.isNotEmpty()) {
                val loginDto = args[0]
                logger.info("User login activity detected")
                // 실제 로깅은 UserService에서 처리됨
            }
        } catch (e: Exception) {
            logger.warn("Failed to log user login activity", e)
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.user.UserService.registerProcess(..))",
        returning = "result"
    )
    fun logUserRegistration(joinPoint: JoinPoint, result: Any) {
        try {
            val args = joinPoint.args
            if (args.isNotEmpty()) {
                logger.info("User registration activity detected")
                // 실제 로깅은 UserService에서 처리됨
            }
        } catch (e: Exception) {
            logger.warn("Failed to log user registration activity", e)
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.user.UserService.follow(..))",
        returning = "result"
    )
    fun logUserFollow(joinPoint: JoinPoint, result: Any) {
        try {
            logger.info("User follow activity detected")
            // 실제 로깅은 UserService에서 처리됨
        } catch (e: Exception) {
            logger.warn("Failed to log user follow activity", e)
        }
    }

    /**
     * 게시물 관련 활동 로깅
     */
    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.post.PostService.createPost(..))",
        returning = "result"
    )
    fun logPostCreation(joinPoint: JoinPoint, result: Any) {
        try {
            logger.info("Post creation activity detected")
            // 실제 로깅은 PostService에서 처리됨
        } catch (e: Exception) {
            logger.warn("Failed to log post creation activity", e)
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.post.PostService.getPostById(..))",
        returning = "result"
    )
    fun logPostView(joinPoint: JoinPoint, result: Any) {
        try {
            logger.debug("Post view activity detected")
            // 실제 로깅은 PostService에서 처리됨
        } catch (e: Exception) {
            logger.warn("Failed to log post view activity", e)
        }
    }

    /**
     * 댓글 관련 활동 로깅
     */
    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.post.CommentService.createComment(..))",
        returning = "result"
    )
    fun logCommentCreation(joinPoint: JoinPoint, result: Any) {
        try {
            logger.info("Comment creation activity detected")
        } catch (e: Exception) {
            logger.warn("Failed to log comment creation activity", e)
        }
    }

    /**
     * 좋아요 관련 활동 로깅
     */
    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.post.PostLikeService.likePost(..))",
        returning = "result"
    )
    fun logPostLike(joinPoint: JoinPoint, result: Any) {
        try {
            logger.info("Post like activity detected")
        } catch (e: Exception) {
            logger.warn("Failed to log post like activity", e)
        }
    }

    /**
     * 검색 관련 활동 로깅
     */
    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.post.PostService.findPostsByTagName(..))",
        returning = "result"
    )
    fun logSearchActivity(joinPoint: JoinPoint, result: Any) {
        try {
            val args = joinPoint.args
            if (args.isNotEmpty()) {
                val searchRequest = args[0]
                logger.info("Search activity detected")
                // 검색 키워드 로깅 로직 추가 가능
            }
        } catch (e: Exception) {
            logger.warn("Failed to log search activity", e)
        }
    }

    /**
     * 스팟 관련 활동 로깅
     */
    @AfterReturning(
        pointcut = "execution(* com.ssuspot.sns.application.service.spot.SpotService.createSpot(..))",
        returning = "result"
    )
    fun logSpotCreation(joinPoint: JoinPoint, result: Any) {
        try {
            logger.info("Spot creation activity detected")
        } catch (e: Exception) {
            logger.warn("Failed to log spot creation activity", e)
        }
    }
}