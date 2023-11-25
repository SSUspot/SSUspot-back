package com.ssuspot.sns.api.exception

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import com.ssuspot.sns.domain.exceptions.post.AccessCommentWithoutNoAuthException
import com.ssuspot.sns.domain.exceptions.post.AccessPostWithNoAuthException
import com.ssuspot.sns.domain.exceptions.post.NotPostOwnerException
import com.ssuspot.sns.domain.exceptions.post.NotCommentOwnerException
import com.ssuspot.sns.domain.exceptions.user.AlreadyFollowedUserException
import com.ssuspot.sns.domain.exceptions.user.EmailExistException
import com.ssuspot.sns.domain.exceptions.user.UserNotFoundException
import com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(AlreadyFollowedUserException::class)
    fun handleAlreadyFollowedUserException(
        exception: AlreadyFollowedUserException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.ALREADY_FOLLOWED_USER.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        exception: UserNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.NOT_EXISTS_USER.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(UserPasswordIncorrectException::class)
    fun handleUserPasswordIncorrectException(
        exception: UserPasswordIncorrectException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_USER_PASSWORD.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(EmailExistException::class)
    fun handleEmailExistException(
        exception: EmailExistException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.ALREADY_EXISTS_USER.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(NotPostOwnerException::class)
    fun handleNotArticleOwnerException(
        exception: NotPostOwnerException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.POST_PERMISSION_DENIED.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(NotCommentOwnerException::class)
    fun handleNotCommentOwnerException(
        exception: NotCommentOwnerException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.COMMENT_PERMISSION_DENIED.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(
        exception: SignatureException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_TOKEN.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(
        exception: MalformedJwtException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_TOKEN.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(DecodingException::class)
    fun handleDecodingException(
        exception: DecodingException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_TOKEN.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errorCode,
                    uri
                )
            )
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(
        exception: ExpiredJwtException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_TOKEN.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errorCode,
                    uri
                )
            )
    }
    @ExceptionHandler(AccessCommentWithoutNoAuthException::class, AccessPostWithNoAuthException::class)
    fun handleAccessCommentWithoutNoAuthException(
        exception: AccessCommentWithoutNoAuthException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.ACCESS_WITHOUT_NO_AUTH.message
        val uri = (request as ServletWebRequest).request.requestURI
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    errorCode,
                    uri
                )
            )
    }
}
