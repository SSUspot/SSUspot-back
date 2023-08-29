package com.ssuspot.sns.core.post.model.entity

import com.ssuspot.sns.core.spot.model.entity.Spot
import jakarta.persistence.*

@Entity
@Table(name = "post_spots")
class PostSpot(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "post_id")
        val post: Post,

        @field:ManyToOne(fetch = FetchType.LAZY)
        @field:JoinColumn(name = "spot_id")
        val spot: Spot,
)