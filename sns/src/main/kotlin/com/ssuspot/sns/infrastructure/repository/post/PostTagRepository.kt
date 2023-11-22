package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostTagRepository
import org.springframework.data.jpa.repository.JpaRepository

interface PostTagRepository: JpaRepository<PostTag, Long>, CustomPostTagRepository