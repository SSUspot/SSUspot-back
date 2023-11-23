package com.ssuspot.sns.api.request.post

data class UpdatePostRequest(
    val title: String,
    val content: String,
    val tags: List<String>,
)