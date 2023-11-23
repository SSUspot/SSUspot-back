package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.PostTag

interface CustomPostTagRepository {
    fun findPostTagsByTagTagName(tagName: String): List<PostTag>?
}