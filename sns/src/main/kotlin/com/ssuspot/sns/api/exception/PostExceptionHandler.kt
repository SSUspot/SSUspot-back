package com.ssuspot.sns.api.exception

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import com.ssuspot.sns.domain.exceptions.post.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PostExceptionHandler {
    @ExceptionHandler(PostNotFoundException::class)
    fun postNotFoundException(
        exception: PostNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.NOT_EXISTS_POST.message
        return ResponseEntity.status(404).body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(InvalidTextException::class)
    fun invalidTextException(
        exception: InvalidTextException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_TEXT.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(NotPostOwnerException::class)
    fun notPostOwnerException(
        exception: NotPostOwnerException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.POST_PERMISSION_DENIED.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(PostTagNotFoundException::class)
    fun postTagNotFoundException(
        exception: PostTagNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.NOT_EXISTS_POST.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(TagNotFoundException::class)
    fun tagNotFoundException(
        exception: TagNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.NOT_EXISTS_POST.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }
}
