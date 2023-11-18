package com.ssuspot.sns.application.dto.common

data class JwtTokenDto(
        val token: String,
        val expiredIn: Long
)