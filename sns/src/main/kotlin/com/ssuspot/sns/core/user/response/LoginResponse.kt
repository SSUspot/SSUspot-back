package com.ssuspot.sns.core.user.response

import com.ssuspot.sns.core.user.model.dto.AuthTokenDto

class LoginResponse(
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
                    token.refreshToken!!.expiredIn
            )
}