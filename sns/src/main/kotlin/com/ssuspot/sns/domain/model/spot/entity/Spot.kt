package com.ssuspot.sns.domain.model.spot.entity

import com.ssuspot.sns.domain.model.post.entity.Post
import javax.persistence.*

@Entity
@Table(name = "spots")
class Spot(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? =null,

        @field:Column(name = "spot_name")
        var spotName: String,

        @field:Column(name = "spot_thumbnail_image_link")
        var spotThumbnailImageLink: String,

        @field:Column(name = "spot_address")
        var spotAddress: String,

        @field:Lob
        @field:Column(name = "spot_info")
        var spotInfo: String,

        @field:Column(name = "spot_level")
        var spotLevel: Int,

        //위도 경도 값 저장
        @field:Column(name = "latitude")
        val latitude: Double,

        @field:Column(name = "longitude")
        val longitude: Double,

        @field:OneToMany(mappedBy = "spot", cascade = [CascadeType.ALL])
        var posts: MutableList<Post> = mutableListOf()
)