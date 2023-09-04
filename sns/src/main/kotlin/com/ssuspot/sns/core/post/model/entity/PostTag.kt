package com.ssuspot.sns.core.post.model.entity

import com.ssuspot.sns.core.tag.model.entity.Tag
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