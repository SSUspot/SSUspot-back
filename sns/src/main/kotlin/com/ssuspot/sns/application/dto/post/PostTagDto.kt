package com.ssuspot.sns.application.dto.post

import com.ssuspot.sns.domain.model.post.entity.PostTag

data class PostTagDto(
    val postId: Long,
    val tagId: Long,
)