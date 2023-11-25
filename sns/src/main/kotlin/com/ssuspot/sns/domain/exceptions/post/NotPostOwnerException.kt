package com.ssuspot.sns.domain.exceptions.post

class NotPostOwnerException : RuntimeException("해당 게시글의 작성자가 아닙니다.")