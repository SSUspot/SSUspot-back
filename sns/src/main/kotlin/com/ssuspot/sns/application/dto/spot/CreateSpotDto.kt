package com.ssuspot.sns.application.dto.spot

data class CreateSpotDto(
        val spotName: String,
        val spotThumbnailImageLink:String,
        val spotAddress: String,
        val spotInfo:String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)