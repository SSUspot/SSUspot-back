package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository:JpaRepository<Post,Long>
