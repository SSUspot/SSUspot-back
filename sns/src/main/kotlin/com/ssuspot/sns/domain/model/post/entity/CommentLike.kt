package com.ssuspot.sns.domain.model.post.entity

import com.ssuspot.sns.domain.model.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "comment_likes")
class CommentLike(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? =null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "user_id")
    val user: User,

    //어떤 게시글에 좋아요한건지 저장
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "comment_id")
    val comment: Comment,
)