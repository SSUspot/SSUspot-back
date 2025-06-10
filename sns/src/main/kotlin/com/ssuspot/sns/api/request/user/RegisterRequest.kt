package com.ssuspot.sns.api.request.user

import jakarta.validation.constraints.*

data class RegisterRequest (
        @field:NotBlank(message = "이메일은 필수입니다")
        @field:Email(message = "올바른 이메일 형식이 아닙니다")
        @field:Size(max = 100, message = "이메일은 100자 이하여야 합니다")
        val email: String,
        
        @field:NotBlank(message = "비밀번호는 필수입니다")
        @field:Pattern(
            regexp = PASSWORD_REGEX, 
            message = "비밀번호는 8자 이상, 영문 대소문자, 숫자, 특수문자를 포함해야 합니다"
        )
        @field:Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다")
        val password: String,
        
        @field:NotBlank(message = "사용자명은 필수입니다")
        @field:Pattern(
            regexp = USERNAME_REGEX,
            message = "사용자명은 3-20자의 영문, 숫자, ., _, -만 사용 가능합니다"
        )
        @field:Size(min = 3, max = 20, message = "사용자명은 3자 이상 20자 이하여야 합니다")
        val userName: String,
        
        @field:NotBlank(message = "닉네임은 필수입니다")
        @field:Pattern(
            regexp = NICKNAME_REGEX,
            message = "닉네임은 2-10자의 한글, 영문, 숫자만 사용 가능합니다"
        )
        @field:Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다")
        val nickname: String,
        
        @field:Size(max = 200, message = "프로필 메시지는 200자 이하여야 합니다")
        @field:Pattern(
            regexp = "^[^<>\"'&\u0000]*$",
            message = "프로필 메시지에 허용되지 않는 문자가 포함되어 있습니다"
        )
        val profileMessage: String?,
        
        val profileImageLink: String?
) {
    companion object {
        const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,100}$"
        const val USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,20}$"
        const val NICKNAME_REGEX = "^[가-힯a-zA-Z0-9 ]{2,10}$"
    }
}