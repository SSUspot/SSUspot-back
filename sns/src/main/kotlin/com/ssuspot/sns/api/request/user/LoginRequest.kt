package com.ssuspot.sns.api.request.user

data class LoginRequest(
        val email: String,
        val password: String
)