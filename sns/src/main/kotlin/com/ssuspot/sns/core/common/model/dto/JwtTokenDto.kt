package com.ssuspot.sns.core.common.model.dto

class JwtTokenDto(
        val token: String,
        val expiredIn: Long
)