package com.ssuspot.sns.core.spot.model.entity

import com.ssuspot.sns.core.post.model.entity.Post
import javax.persistence.*

@Entity
@Table(name = "spots")
class Spot(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? =null,

        @field:Column(name = "spot_name")
        var spotName: String,

        //위도 경도 값 저장
        @field:Column(name = "latitude")
        var latitude: Double,

        @field:Column(name = "longitude")
        var longitude: Double,

        @field:OneToMany(mappedBy = "spot", cascade = [CascadeType.ALL])
        var posts: MutableList<Post> = mutableListOf()
)