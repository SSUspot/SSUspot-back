package com.ssuspot.sns.api.response.user

import com.ssuspot.sns.application.dto.user.AuthTokenDto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 응답")
data class LoginResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        val accessToken: String,
        
        @Schema(description = "액세스 토큰 만료 시간(밀리초)", example = "86400000")
        val accessTokenExpiredIn: Long,
        
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        val refreshToken: String,
        
        @Schema(description = "리프레시 토큰 만료 시간(밀리초)", example = "2592000000")
        val refreshTokenExpiredIn: Long
) {
    constructor(token: AuthTokenDto) :
            this(
                    token.accessToken.token,
                    token.accessToken.expiredIn,
                    token.refreshToken!!.token,
                    token.refreshToken.expiredIn
            )
}