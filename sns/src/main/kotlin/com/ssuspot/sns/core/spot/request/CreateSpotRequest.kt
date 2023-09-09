package com.ssuspot.sns.core.spot.request

class CreateSpotRequest(
        val spotName: String,
        val spotThumbnailImageLink:String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)