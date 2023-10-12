package com.ssuspot.sns.application.dto.spot

data class SpotResponseDto(
        val id: Long?,
        val spotName: String,
        val spotThumbnailImageLink: String,
        val spotLevel: Int,
        val latitude: Double,
        val longitude: Double
)