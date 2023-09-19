package com.ssuspot.sns.api.exception

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import com.ssuspot.sns.domain.exceptions.spot.SpotNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpotExceptionHandler {
    @ExceptionHandler(SpotNotFoundException::class)
    fun handleSpotNotFoundException(
        exception: SpotNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.SPOT_NOT_EXISTS.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }
}