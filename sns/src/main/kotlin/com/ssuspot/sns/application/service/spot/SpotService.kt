package com.ssuspot.sns.application.service.spot

import com.ssuspot.sns.application.dto.spot.CreateSpotDto
import com.ssuspot.sns.application.dto.spot.SpotResponseDto
import com.ssuspot.sns.domain.exceptions.spot.SpotNotFoundException
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.infrastructure.repository.spot.SpotRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpotService(
    val spotRepository: SpotRepository,
) {
    //create spot
    @Transactional
    @CacheEvict(value = ["spots"], allEntries = true)
    fun createSpot(
            createSpotDto: CreateSpotDto
    ): SpotResponseDto {
        val savedSpot = spotRepository.save(
                Spot(
                        spotName = createSpotDto.spotName,
                        spotThumbnailImageLink = createSpotDto.spotThumbnailImageLink,
                        spotAddress = createSpotDto.spotAddress,
                        spotInfo = createSpotDto.spotInfo,
                        spotLevel = createSpotDto.spotLevel,
                        latitude = createSpotDto.latitude,
                        longitude = createSpotDto.longitude
                )
        )
        return SpotResponseDto(
                savedSpot.id,
                savedSpot.spotName,
                savedSpot.spotThumbnailImageLink,
                savedSpot.spotAddress,
                savedSpot.spotInfo,
                savedSpot.spotLevel,
                savedSpot.latitude,
                savedSpot.longitude
        )
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["spots"], key = "#spotId")
    fun getSpot(spotId: Long): SpotResponseDto{
        val foundSpot = findValidSpot(spotId)
        return SpotResponseDto(
                foundSpot.id,
                foundSpot.spotName,
                foundSpot.spotThumbnailImageLink,
                foundSpot.spotAddress,
                foundSpot.spotInfo,
                foundSpot.spotLevel,
                foundSpot.latitude,
                foundSpot.longitude
        )
    }

    //temp get all spot method
    @Transactional(readOnly = true)
    @Cacheable(value = ["spots"], key = "'all'")
    fun getAllSpot(): List<Spot> {
        return spotRepository.findAll()
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["spots"], key = "'entity_' + #spotId")
    fun findValidSpot(spotId: Long): Spot {
        return spotRepository.findSpotById(spotId) ?: throw SpotNotFoundException()
    }
}