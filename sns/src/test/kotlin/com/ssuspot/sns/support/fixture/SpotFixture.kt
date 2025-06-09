package com.ssuspot.sns.support.fixture

import com.ssuspot.sns.domain.model.spot.entity.Spot

object SpotFixture {
    
    fun createSpot(
        id: Long? = 1L,
        spotName: String = "Test Spot",
        spotInfo: String = "This is a test spot",
        latitude: Double = 37.5665,
        longitude: Double = 126.9780,
        spotAddress: String = "Seoul, South Korea",
        spotThumbnailImageLink: String = "https://example.com/spot.jpg",
        spotLevel: Int = 1
    ): Spot {
        return Spot(
            id = id,
            spotName = spotName,
            spotThumbnailImageLink = spotThumbnailImageLink,
            spotAddress = spotAddress,
            spotInfo = spotInfo,
            spotLevel = spotLevel,
            latitude = latitude,
            longitude = longitude
        )
    }
    
    fun createSpots(count: Int): List<Spot> {
        return (1..count).map { i ->
            createSpot(
                id = i.toLong(),
                spotName = "Test Spot $i",
                spotInfo = "This is test spot $i",
                latitude = 37.5665 + (i * 0.001),
                longitude = 126.9780 + (i * 0.001)
            )
        }
    }
}