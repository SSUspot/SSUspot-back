package com.ssuspot.sns.application.dto.user

import com.ssuspot.sns.application.dto.common.JwtTokenDto

class AuthTokenDto(
    val accessToken: JwtTokenDto,
    val refreshToken: JwtTokenDto?
)