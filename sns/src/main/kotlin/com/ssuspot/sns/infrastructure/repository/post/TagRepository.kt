package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.domain.model.post.repository.CustomTagRepository
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long>, CustomTagRepository {
    fun findTagByName(name: String): Tag?
}