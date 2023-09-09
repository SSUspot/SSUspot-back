package com.ssuspot.sns.core.user.model.dto

import com.ssuspot.sns.core.common.model.dto.JwtTokenDto

class AuthTokenDto(
    val accessToken: JwtTokenDto,
    val refreshToken: JwtTokenDto?
)