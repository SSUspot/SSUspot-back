package com.ssuspot.sns.application.service.spot

import com.ssuspot.sns.application.dto.spot.CreateSpotDto
import com.ssuspot.sns.application.dto.spot.CreateSpotResponseDto
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.infrastructure.repository.spot.SpotRepository
import org.springframework.stereotype.Service

@Service
class SpotService(
    val spotRepository: SpotRepository,
) {
    //create spot
    fun createSpot(
            createSpotDto: CreateSpotDto
    ): CreateSpotResponseDto {
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

    fun findValidSpot(spotId: Long): Spot {
        return spotRepository.findById(spotId)
                .orElseThrow { throw Exception("spot not found") }
    }
}