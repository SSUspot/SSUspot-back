package com.ssuspot.sns.application.dto.user

import com.ssuspot.sns.application.dto.common.JwtTokenDto

data class AuthTokenDto(
    val accessToken: JwtTokenDto,
    val refreshToken: JwtTokenDto?
)