package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomTagRepository {
    fun findPostsByTagName(tagName: String, page: Pageable): Page<Post>?
    fun findTagByTagName(name: String): Tag?
    fun findTagsByTagNameIn(names: List<String>): List<Tag>?
    fun findPostsByTagNameIn(names: List<String>, page: Pageable): Page<Post>?
}

