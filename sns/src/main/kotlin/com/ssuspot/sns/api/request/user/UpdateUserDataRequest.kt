package com.ssuspot.sns.api.request.user

data class UpdateUserDataRequest(
    val userName : String,
    val nickname: String,
    val profileMessage: String?,
    val profileImageLink: String?
)