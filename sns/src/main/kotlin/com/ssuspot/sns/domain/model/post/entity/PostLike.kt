package com.ssuspot.sns.domain.model.post.entity

import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.user.entity.User
import jakarta.persistence.*

@Entity
@Table(
    name = "post_likes",
    indexes = [
        Index(name = "idx_post_likes_post_id", columnList = "post_id"),
        Index(name = "idx_post_likes_user_id", columnList = "user_id"),
        Index(name = "idx_post_likes_post_user", columnList = "post_id, user_id")
    ]
)
class PostLike(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? =null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "user_id")
    val user: User,

    //어떤 게시글에 좋아요한건지 저장
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "post_id")
    val post: Post,
): BaseTimeEntity(){
    constructor(user: User, post: Post): this(null, user, post)
}