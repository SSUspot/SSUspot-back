package com.ssuspot.sns.domain.exceptions.post

class AccessPostWithNoAuthException : RuntimeException("요청한 유저는 글 작성자가 아닙니다.")