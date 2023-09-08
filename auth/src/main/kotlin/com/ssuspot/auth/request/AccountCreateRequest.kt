package com.ssuspot.auth.request

import javax.validation.constraints.Pattern

class AccountCreateRequest(
        val email: String,
        @field:Pattern(regexp = PASSWORD_REGEX, message = "유효하지 않은 비밀번호입니다.")
        val password: String,
        val nickname: String,
) {
    companion object {
        const val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[!-~₩]{8,100}$"
    }
}