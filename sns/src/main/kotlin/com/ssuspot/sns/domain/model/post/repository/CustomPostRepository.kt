package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostRepository {
    fun save(post: Post): Post
    fun delete(post: Post)
    fun findValidPostById(postId: Long): Post
    fun findPostById(postId: Long, user: User): PostResponseDto?
    fun findPostsBySpotId(spotId: Long, user: User, pageable: Pageable): Page<PostResponseDto>

    fun findPostsByUserId(user: User, pageable: Pageable): Page<PostResponseDto>
    fun findPostsByTagName(tagName: String, user: User, pageable: Pageable): Page<PostResponseDto>?

    fun findPostsByFollowingUsers(user: User, pageable: Pageable): Page<PostResponseDto>

    fun findRecommendedPosts(user: User, pageable: Pageable): Page<PostResponseDto>
}