package com.ssuspot.sns.domain.model.post.entity

import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import com.ssuspot.sns.domain.model.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "images")
class Image(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "image_url")
    var imageUrl: String,

    @field:Column(name = "latitude")
    var latitude: Double,

    @field:Column(name = "longitude")
    var longitude: Double,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "user_id")
    val user: User,
): BaseTimeEntity()