package com.ssuspot.sns.application.dto.common

class JwtTokenDto(
        val token: String,
        val expiredIn: Long
)