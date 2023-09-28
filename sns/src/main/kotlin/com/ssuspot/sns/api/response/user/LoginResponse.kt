package com.ssuspot.sns.api.response.user

import com.ssuspot.sns.application.dto.user.AuthTokenDto

data class LoginResponse(
        val accessToken: String,
        val accessTokenExpiredIn: Long,
        val refreshToken: String,
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