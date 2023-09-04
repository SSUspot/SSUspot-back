package com.ssuspot.sns.core.post.model.entity

import com.ssuspot.sns.core.comment.model.entity.Comment
import com.ssuspot.sns.core.like.model.entity.Like
import com.ssuspot.sns.core.user.model.entity.User
import javax.persistence.*

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

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val comments: MutableList<Comment> = mutableListOf(),

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val likes: MutableList<Like> = mutableListOf(),

    @field:OneToOne(mappedBy = "post", cascade = [CascadeType.ALL])
    val postSpot: PostSpot,

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val postTags: MutableList<PostTag> = mutableListOf()
)