package com.ssuspot.sns.domain.model.post.entity

import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.user.entity.User
import javax.persistence.*

@Entity
@Table(name = "comments")
class Comment(
    @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "post_id")
        val post: Post,

    @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "user_id")
        val user: User,

    @field:Lob
        @field:Column(name = "content")
        val content: String,
) : BaseTimeEntity()
