package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomCommentRepository {
    fun findCommentById(commentId: Long): Comment?
    fun findCommentsByPostId(postId: Long, pageable: Pageable): Page<Comment>
    fun findCommentsByUserId(userId: Long, pageable: Pageable): Page<Comment>
}