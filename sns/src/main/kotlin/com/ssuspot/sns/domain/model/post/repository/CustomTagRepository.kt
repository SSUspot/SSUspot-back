package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.post.entity.Tag

interface CustomTagRepository {
    fun findTagByTagName(name: String): Tag?
    fun findTagsByTagNameIn(names: List<String>): List<Tag>?
}

