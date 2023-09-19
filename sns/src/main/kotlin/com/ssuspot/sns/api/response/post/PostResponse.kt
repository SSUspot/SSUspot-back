package com.ssuspot.sns.api.response.post

// TODO: image Url 구현 부분 수정
class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: String,
    val imageUrls: String,
    val spotId: Long
)