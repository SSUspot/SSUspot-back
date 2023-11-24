package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.PostLike
import com.ssuspot.sns.domain.model.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface PostLikeRepository: JpaRepository<PostLike, Long> {
    @Transactional
    fun deleteByPostAndUser(post: Post, user: User)
    fun findByPostAndUser(post: Post, user: User): PostLike?
}