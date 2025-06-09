package com.ssuspot.sns.domain.exceptions.user

import com.ssuspot.sns.domain.exceptions.code.ApiErrorCode

class InvalidRefreshTokenException : RuntimeException("유효하지 않은 리프레시 토큰입니다.") {
    val errorCode = ApiErrorCode.VALIDATION_ERROR
}