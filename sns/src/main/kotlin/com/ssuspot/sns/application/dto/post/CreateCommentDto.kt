package com.ssuspot.sns.application.dto.post

import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User

data class CreateCommentDto(
    val postId: Long,
    val userEmail: String,
    val content: String,
) {
    fun toEntity(post: Post, user: User): Comment =
        Comment(
            post = post,
            user = user,
            content = content,
        )
}