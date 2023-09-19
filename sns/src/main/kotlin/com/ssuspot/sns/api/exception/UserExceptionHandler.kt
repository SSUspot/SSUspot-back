package com.ssuspot.sns.api.exception

import com.ssuspot.sns.api.response.common.ErrorResponse
import com.ssuspot.sns.domain.exceptions.code.ErrorCode
import com.ssuspot.sns.domain.exceptions.user.EmailExistException
import com.ssuspot.sns.domain.exceptions.user.UserNotFoundException
import com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException
import com.ssuspot.sns.domain.exceptions.user.UserRegisterFailedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        exception: UserNotFoundException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.NOT_EXISTS_USER.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }
    @ExceptionHandler(UserPasswordIncorrectException::class)
    fun handleUserPasswordIncorrectException(
        exception: UserPasswordIncorrectException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.INVALID_USER_PASSWORD.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(EmailExistException::class)
    fun handleEmailExistException(
        exception: EmailExistException
    ): ResponseEntity<ErrorResponse> {
        val errorCode: String = ErrorCode.ALREADY_EXISTS_USER.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }

    @ExceptionHandler(UserRegisterFailedException::class)
    fun handleUserRegisterFailedException(
        exception: UserRegisterFailedException
    ): ResponseEntity<ErrorResponse>{
        val errorCode: String = ErrorCode.UNKNOWN.message
        return ResponseEntity.badRequest().body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorCode,
                exception.message ?: ""
            )
        )
    }
}