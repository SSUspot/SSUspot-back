package com.ssuspot.sns.domain.exceptions.post

class AccessCommentWithoutNoAuthException : RuntimeException("요청한 유저는 댓글 작성자가 아닙니다.")