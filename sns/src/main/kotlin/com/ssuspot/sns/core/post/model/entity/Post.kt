package com.ssuspot.sns.core.post.model.entity

import com.ssuspot.sns.core.user.model.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "posts")
class Post(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "user_id")
        val user: User,

        @field:Column(name = "title")
        var title: String,

        @field:Lob
        @field:Column(name = "content")
        val content: String,

        //TODO: 조회수 집계 방식의 변화
        @field:Column(name = "view_count")
        var viewCount: Long = 0,
)