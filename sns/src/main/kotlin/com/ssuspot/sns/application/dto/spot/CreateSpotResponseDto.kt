package com.ssuspot.sns.application.dto.spot

class CreateSpotResponseDto(
        val id: Long?,
        val spotName: String,
        val spotThumbnailImageLink: String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)