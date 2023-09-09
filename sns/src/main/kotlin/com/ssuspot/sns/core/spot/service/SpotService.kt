package com.ssuspot.sns.core.spot.service

import com.ssuspot.sns.core.spot.model.dto.CreateSpotDto
import com.ssuspot.sns.core.spot.model.dto.CreateSpotResponseDto
import com.ssuspot.sns.core.spot.model.entity.Spot
import com.ssuspot.sns.core.spot.model.repository.SpotRepository
import org.springframework.stereotype.Service

@Service
class SpotService(
        val spotRepository: SpotRepository,
) {
    //create spot
    fun createSpot(
            createSpotDto: CreateSpotDto
    ):CreateSpotResponseDto{
        val savedSpot = spotRepository.save(
                Spot(
                        spotName = createSpotDto.spotName,
                        spotThumbnailImageLink = createSpotDto.spotThumbnailImageLink,
                        spotLevel = createSpotDto.spotLevel,
                        latitude = createSpotDto.latitude,
                        longitude = createSpotDto.longitude
                )
        )
        return CreateSpotResponseDto(
                savedSpot.id,
                savedSpot.spotName,
                savedSpot.spotThumbnailImageLink,
                savedSpot.spotLevel,
                savedSpot.latitude,
                savedSpot.longitude
        )
    }
    //get spot list
}