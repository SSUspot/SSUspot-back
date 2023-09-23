package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
interface CustomPostRepository {
    fun findPostById(postId: Long): Post?
    fun findPostsBySpotId(spotId: Long, pageable: Pageable): Page<Post>

    fun findPostsByUserId(userId: Long, pageable: Pageable): Page<Post>
}