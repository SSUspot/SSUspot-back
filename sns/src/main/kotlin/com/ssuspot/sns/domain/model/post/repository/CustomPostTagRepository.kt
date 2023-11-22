package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.PostTag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPostTagRepository {
    fun findPostTagsByPostId(postId: Long, pageable: Pageable): Page<PostTag>?
    fun findPostTagsByTagTagName(tagName: String): List<PostTag>?
}