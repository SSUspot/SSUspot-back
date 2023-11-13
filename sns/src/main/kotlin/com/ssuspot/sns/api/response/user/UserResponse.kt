package com.ssuspot.sns.api.response.user

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponse (
    val id: Long,
    val email: String,
    val userName: String,
    val nickname: String,
    val profileMessage: String?,
    val profileImageLink: String?
)