package com.ssuspot.sns.domain.model.post.entity

import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.domain.model.user.entity.User
import javax.persistence.*

@Entity
@Table(name = "posts")
class Post(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "user_id")
    val user: User,

    @field:Column(name = "title")
    var title: String,

    @field:Lob
    @field:Column(name = "content")
    var content: String,

    // 조회수 집계 방식의 변화
    @field:Column(name = "view_count")
    var viewCount: Long = 0,

    // 좋아요 수 집계 방식 고민하기
    @field:Column(name = "like_count")
    var likeCount: Long = 0,

    @field:Column(name = "rating")
    var rating: Double? = 0.0,

    @field:Column(name = "image_url")
    var imageUrl: String = "",

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val comments: MutableList<Comment> = mutableListOf(),

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val postLikes: MutableList<PostLike> = mutableListOf(),

    @field:OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    var postTags: MutableList<PostTag> = mutableListOf(),

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "spot_id")
    val spot: Spot

) : BaseTimeEntity() {
    fun toDto(): PostResponseDto =
        PostResponseDto(
            id = id!!,
            title = title,
            content = content,
            nickname = user.nickname,
            imageUrl = imageUrl,
            tags = postTags.map { it.tag.tagName },
            spotId = spot.id!!,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString()
        )
}
