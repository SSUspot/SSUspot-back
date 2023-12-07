package com.ssuspot.sns.api.request.spot

data class CreateSpotRequest(
        val spotName: String,
        val spotThumbnailImageLink:String,
        val spotAddress: String,
        val spotInfo:String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)