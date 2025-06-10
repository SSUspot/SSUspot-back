package com.ssuspot.sns.infrastructure.logging

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import com.ssuspot.sns.domain.exceptions.post.*
import com.ssuspot.sns.domain.exceptions.spot.SpotNotFoundException
import com.ssuspot.sns.domain.exceptions.user.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.resource.NoResourceFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import java.sql.SQLException
import java.time.Instant

/**
 * 전역 예외 처리 및 로깅을 담당하는 핸들러
 * 모든 예외를 구조화된 형태로 로그에 기록하고 적절한 응답을 반환합니다.
 */
@RestControllerAdvice
class GlobalExceptionHandler(
    private val loggerFactory: StructuredLoggerFactory
) {

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(
        UserNotFoundException::class,
        PostNotFoundException::class,
        SpotNotFoundException::class,
        CommentNotFoundException::class,
        TagNotFoundException::class,
        PostTagNotFoundException::class
    )
    fun handleNotFoundException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logBusinessException(ex, request, "RESOURCE_NOT_FOUND")
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.NOT_FOUND,
            errCode = "RESOURCE_NOT_FOUND",
            message = ex.message ?: "요청한 리소스를 찾을 수 없습니다."
        )
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * 인증/인가 예외 처리
     */
    @ExceptionHandler(
        BadCredentialsException::class,
        UserPasswordIncorrectException::class
    )
    fun handleAuthenticationException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logSecurityException(ex, request, SecurityEvent.LOGIN_FAILURE)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.UNAUTHORIZED,
            errCode = "AUTHENTICATION_FAILED",
            message = "인증에 실패했습니다."
        )
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logSecurityException(ex, request, SecurityEvent.UNAUTHORIZED_ACCESS)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.FORBIDDEN,
            errCode = "ACCESS_DENIED",
            message = "접근 권한이 없습니다."
        )
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    /**
     * 권한 관련 예외 처리
     */
    @ExceptionHandler(
        NotPostOwnerException::class,
        NotCommentOwnerException::class,
        AccessPostWithNoAuthException::class,
        AccessCommentWithoutNoAuthException::class
    )
    fun handlePermissionException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logSecurityException(ex, request, SecurityEvent.UNAUTHORIZED_ACCESS)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.FORBIDDEN,
            errCode = "PERMISSION_DENIED",
            message = ex.message ?: "해당 작업을 수행할 권한이 없습니다."
        )
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    /**
     * 비즈니스 규칙 위반 예외 처리
     */
    @ExceptionHandler(
        EmailExistException::class,
        AlreadyFollowedUserException::class,
        UserRegisterFailedException::class
    )
    fun handleBusinessRuleException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logBusinessException(ex, request, "BUSINESS_RULE_VIOLATION")
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.CONFLICT,
            errCode = "BUSINESS_RULE_VIOLATION",
            message = ex.message ?: "비즈니스 규칙에 위반됩니다."
        )
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    /**
     * 입력 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.associate { 
            it.field to (it.defaultMessage ?: "유효하지 않은 값입니다.")
        }
        
        logValidationException(ex, request, fieldErrors)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            errCode = "VALIDATION_FAILED",
            message = "입력 데이터 검증에 실패했습니다.",
            details = fieldErrors
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val violations = ex.constraintViolations.associate {
            it.propertyPath.toString() to it.message
        }
        
        logValidationException(ex, request, violations)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            errCode = "CONSTRAINT_VIOLATION",
            message = "제약 조건 위반입니다.",
            details = violations
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(InvalidTextException::class)
    fun handleInvalidTextException(
        ex: InvalidTextException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logSecurityException(ex, request, SecurityEvent.INPUT_VALIDATION_FAILURE)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.BAD_REQUEST,
            errCode = "INVALID_INPUT",
            message = ex.message ?: "입력된 텍스트가 유효하지 않습니다."
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * 데이터베이스 예외 처리
     */
    @ExceptionHandler(SQLException::class)
    fun handleSQLException(
        ex: SQLException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logDatabaseException(ex, request)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            errCode = "DATABASE_ERROR",
            message = "데이터베이스 오류가 발생했습니다."
        )
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * 일반 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logGenericException(ex, request)
        
        val errorResponse = ErrorResponse(
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            errCode = "INTERNAL_SERVER_ERROR",
            message = "서버 내부 오류가 발생했습니다."
        )
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * 비즈니스 예외 로깅
     */
    private fun logBusinessException(
        ex: Exception,
        request: HttpServletRequest,
        errorType: String
    ) {
        logger.error("Business exception occurred", ex)
        
        loggerFactory.logBusiness(
            action = BusinessAction.READ, // 대부분 조회 시 발생
            entityType = extractEntityType(ex),
            details = mapOf(
                "exception" to ex.javaClass.simpleName,
                "message" to (ex.message ?: "Unknown error"),
                "errorType" to errorType,
                "endpoint" to request.requestURI,
                "method" to request.method,
                "timestamp" to Instant.now().toString()
            )
        )
    }

    /**
     * 보안 예외 로깅
     */
    private fun logSecurityException(
        ex: Exception,
        request: HttpServletRequest,
        securityEvent: SecurityEvent
    ) {
        logger.warn("Security exception occurred", ex)
        
        loggerFactory.logSecurity(
            event = securityEvent,
            clientIp = getClientIpAddress(request),
            details = mapOf(
                "exception" to ex.javaClass.simpleName,
                "message" to (ex.message ?: "Unknown error"),
                "endpoint" to request.requestURI,
                "method" to request.method,
                "userAgent" to (request.getHeader("User-Agent") ?: "Unknown"),
                "timestamp" to Instant.now().toString()
            )
        )
    }

    /**
     * 검증 예외 로깅
     */
    private fun logValidationException(
        ex: Exception,
        request: HttpServletRequest,
        violations: Map<String, String>
    ) {
        logger.warn("Validation exception occurred", ex)
        
        loggerFactory.logSecurity(
            event = SecurityEvent.INPUT_VALIDATION_FAILURE,
            clientIp = getClientIpAddress(request),
            details = mapOf(
                "exception" to ex.javaClass.simpleName,
                "violations" to violations,
                "endpoint" to request.requestURI,
                "method" to request.method,
                "timestamp" to Instant.now().toString()
            )
        )
    }

    /**
     * 데이터베이스 예외 로깅
     */
    private fun logDatabaseException(
        ex: SQLException,
        request: HttpServletRequest
    ) {
        logger.error("Database exception occurred", ex)
        
        loggerFactory.logDatabaseOperation(
            operation = "UNKNOWN",
            table = "UNKNOWN",
            duration = 0,
            success = false,
            details = mapOf(
                "sqlState" to (ex.sqlState ?: "Unknown"),
                "errorCode" to ex.errorCode,
                "message" to (ex.message ?: "Unknown error"),
                "endpoint" to request.requestURI,
                "method" to request.method,
                "timestamp" to Instant.now().toString()
            )
        )
    }

    /**
     * 일반 예외 로깅
     */
    private fun logGenericException(
        ex: Exception,
        request: HttpServletRequest
    ) {
        logger.error("Unexpected exception occurred", ex)
        
        loggerFactory.logPerformance(
            operation = "ERROR_HANDLING",
            duration = 0,
            success = false,
            details = mapOf(
                "exception" to ex.javaClass.simpleName,
                "message" to (ex.message ?: "Unknown error"),
                "endpoint" to request.requestURI,
                "method" to request.method,
                "stackTrace" to ex.stackTrace.take(5).map { it.toString() },
                "timestamp" to Instant.now().toString()
            )
        )
    }

    /**
     * 예외에서 엔티티 타입 추출
     */
    private fun extractEntityType(ex: Exception): String {
        return when (ex) {
            is UserNotFoundException -> "USER"
            is PostNotFoundException -> "POST"
            is SpotNotFoundException -> "SPOT"
            is CommentNotFoundException -> "COMMENT"
            is TagNotFoundException -> "TAG"
            else -> "UNKNOWN"
        }
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private fun getClientIpAddress(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        val xRealIp = request.getHeader("X-Real-IP")
        
        return when {
            !xForwardedFor.isNullOrBlank() -> xForwardedFor.split(",")[0].trim()
            !xRealIp.isNullOrBlank() -> xRealIp
            else -> request.remoteAddr
        }
    }
}