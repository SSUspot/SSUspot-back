package com.ssuspot.sns.application.dto.post

data class UpdatePostRequestDto(
    val title: String,
    val content: String,
    val email: String,
    val tags: List<String>
)