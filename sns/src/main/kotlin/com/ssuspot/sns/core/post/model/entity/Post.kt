package com.ssuspot.sns.core.post.model.entity

import com.ssuspot.sns.core.comment.model.entity.Comment
import com.ssuspot.sns.core.common.model.entity.BaseTimeEntity
import com.ssuspot.sns.core.like.model.entity.Like
import com.ssuspot.sns.core.spot.model.entity.Spot
import com.ssuspot.sns.core.user.model.entity.User
import javax.persistence.*

@Entity
@Table(name = "posts")
class Post(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? =null,

        @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "user_id")
        val user: User,

        @field:Column(name = "title")
        var title: String,

        @field:Lob
        @field:Column(name = "content")
        var content: String,

        //TODO: 조회수 집계 방식의 변화
        @field:Column(name = "view_count")
        var viewCount: Long = 0,

        //TODO: 좋아요 수 집계 방식 고민하기
        @field:Column(name = "like_count")
        var likeCount: Long = 0,

        @field:Column(name = "image_urls")
        var imageUrls: String,

        @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
        val comments: MutableList<Comment> = mutableListOf(),

        @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
        val likes: MutableList<Like> = mutableListOf(),

        @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
        val postTags: MutableList<PostTag> = mutableListOf(),

        @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "spot_id")
        val spot: Spot

) : BaseTimeEntity()