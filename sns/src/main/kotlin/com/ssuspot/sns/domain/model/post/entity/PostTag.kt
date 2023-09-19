package com.ssuspot.sns.domain.model.post.entity

import javax.persistence.*

@Entity
@Table(name = "post_tags")
class PostTag(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "post_id")
    val post: Post,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "tag_id")
    val tag: Tag
)