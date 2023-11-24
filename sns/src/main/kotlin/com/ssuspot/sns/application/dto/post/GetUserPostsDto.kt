package com.ssuspot.sns.application.dto.post

data class GetUserPostsDto(
    val page: Int,
    val size: Int,
    val sort: String,
    // 여기서 userId는 검색 대상 유저의 id임
    val userId: Long,
    // 현재 로그인 한 유저의 email
    val email: String
)