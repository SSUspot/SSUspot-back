package com.ssuspot.sns.domain.model.post.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
@Table(
    name = "tags",
    indexes = [
        Index(name = "idx_tags_tag_name", columnList = "tag_name")
    ]
)
class Tag(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "tag_name")
    val tagName: String,

    @field:OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @field:BatchSize(size = 20)
    val postTags: MutableList<PostTag> = mutableListOf()
)