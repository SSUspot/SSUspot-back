package com.ssuspot.sns.core.spot.model.dto

class CreateSpotDto(
        val spotName: String,
        val spotThumbnailImageLink:String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)