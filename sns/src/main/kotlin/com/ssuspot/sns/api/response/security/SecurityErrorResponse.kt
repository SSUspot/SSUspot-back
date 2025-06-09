package com.ssuspot.sns.api.response.security

data class SecurityErrorResponse(
    val errorCode: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)