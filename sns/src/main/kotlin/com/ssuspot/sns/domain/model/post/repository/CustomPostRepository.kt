package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
interface CustomPostRepository {
    fun findPostByPostId(postId: Long): Post?
    fun findPostsBySpotSpotId(spotId: Long, pageable: Pageable): Page<Post>

    fun findPostsByUserUserId(userId: Long, pageable: Pageable): Page<Post>
}