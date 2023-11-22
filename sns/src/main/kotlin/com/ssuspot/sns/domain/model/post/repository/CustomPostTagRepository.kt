package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.PostTag

interface CustomPostTagRepository {
    fun findPostTagsByPostId(postId: Long): List<PostTag>?
    fun findPostTagsByTagName(tagName: String): List<PostTag>?
}