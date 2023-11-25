package com.ssuspot.sns.api.exception

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import com.ssuspot.sns.domain.exceptions.post.CommentNotFoundException
import com.ssuspot.sns.domain.exceptions.post.NotCommentOwnerException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommentExceptionHandler {
    @ExceptionHandler(CommentNotFoundException::class)
    fun commentNotFoundException(
        exception: CommentNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.NOT_EXISTS_COMMENT.message
        return ResponseEntity.status(404).body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(NotCommentOwnerException::class)
    fun notCommentOwnerException(
        exception: NotCommentOwnerException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.COMMENT_PERMISSION_DENIED.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }
}