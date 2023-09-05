package com.ssuspot.sns.core.like.model.entity

import com.ssuspot.sns.core.post.model.entity.Post
import com.ssuspot.sns.core.user.model.entity.User
import javax.persistence.*

@Entity
@Table(name = "likes")
class Like(
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
)