package com.ssuspot.sns.api.request.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 요청")
data class LoginRequest(
        @Schema(description = "이메일 주소", example = "user@example.com", required = true)
        val email: String,
        
        @Schema(description = "비밀번호", example = "password123", required = true)
        val password: String
)