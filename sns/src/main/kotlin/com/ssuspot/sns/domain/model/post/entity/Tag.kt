package com.ssuspot.sns.domain.model.post.entity

import jakarta.persistence.*

@Entity
@Table(name = "tags")
class Tag(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "tag_name")
    val tagName: String,

    @field:OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL])
    val postTags: MutableList<PostTag> = mutableListOf()
)