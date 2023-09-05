package com.ssuspot.sns.core.comment.model.entity

import com.ssuspot.sns.core.common.model.entity.BaseTimeEntity
import com.ssuspot.sns.core.post.model.entity.Post
import com.ssuspot.sns.core.user.model.entity.User
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
