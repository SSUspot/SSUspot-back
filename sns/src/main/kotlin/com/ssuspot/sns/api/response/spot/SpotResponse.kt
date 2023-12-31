package com.ssuspot.sns.api.response.spot

data class SpotResponse(
    val id: Long,
    val spotName: String,
    val spotThumbnailImageLink: String,
    val spotAddress: String,
    val spotInfo: String,
    val spotLevel: Int,
    val latitude: Double,
    val longitude: Double
)