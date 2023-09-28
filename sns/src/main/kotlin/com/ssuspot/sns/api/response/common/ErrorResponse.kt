package com.ssuspot.sns.api.response.common

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
        val message: String
) {
    constructor(httpStatus: HttpStatus, errCode: String, message: String) : this(
            timestamp = LocalDateTime.now(),
            status = httpStatus.value(),
            error = errCode,
            message = message
    )
}