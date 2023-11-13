package com.ssuspot.sns.api.exception

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.FileNotFoundException

@RestControllerAdvice
class ImageExceptionHandler {
    @ExceptionHandler(FileNotFoundException::class)
    fun fileNotFoundException(
        exception: FileNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.FILE_NOT_FOUND.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: "File을 찾을 수 없습니다"
            )
        )
    }
}