package com.ssuspot.sns.api.request.spot

class CreateSpotRequest(
        val spotName: String,
        val spotThumbnailImageLink:String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)