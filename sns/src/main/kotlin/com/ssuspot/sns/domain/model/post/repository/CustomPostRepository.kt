package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
interface CustomPostRepository {
    fun save(post: Post): Post
    fun delete(post: Post)
    fun findPostById(postId: Long): Post?
    fun findPostsBySpotId(spotId: Long, pageable: Pageable): Page<Post>

    fun findPostsByUserId(userId: Long, pageable: Pageable): Page<Post>
    fun findPostsByTagName(tagName: String, pageable: Pageable): Page<Post>?
    fun findPostsByTagNameIn(tagNames: List<String>, page: Pageable): Page<Post>?
}