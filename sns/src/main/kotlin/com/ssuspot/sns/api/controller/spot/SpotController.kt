package com.ssuspot.sns.api.controller.spot

import com.ssuspot.sns.application.dto.spot.CreateSpotDto
import com.ssuspot.sns.api.request.spot.CreateSpotRequest
import com.ssuspot.sns.api.response.spot.SpotResponse
import com.ssuspot.sns.application.service.spot.SpotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SpotController(
    val spotService: SpotService
) {
    //create spot
    @PostMapping("/api/spots")
    fun createSpot(
        @RequestBody createSpotRequest: CreateSpotRequest
    ): ResponseEntity<SpotResponse> {
        val savedSpot = spotService.createSpot(
            CreateSpotDto(
                createSpotRequest.spotName,
                createSpotRequest.spotThumbnailImageLink,
                createSpotRequest.spotAddress,
                createSpotRequest.spotInfo,
                createSpotRequest.spotLevel,
                createSpotRequest.latitude,
                createSpotRequest.longitude
            )
        )
        return ResponseEntity.ok(
            SpotResponse(
                savedSpot.id!!,
                savedSpot.spotName,
                savedSpot.spotThumbnailImageLink,
                savedSpot.spotAddress,
                savedSpot.spotInfo,
                savedSpot.spotLevel,
                savedSpot.latitude,
                savedSpot.longitude
            )
        )
    }

    //get spot list
    @GetMapping("/api/spots")
    fun getSpotList(

    ): ResponseEntity<List<SpotResponse>> {
        return ResponseEntity.ok(
            spotService.getAllSpot().map {
                SpotResponse(
                    it.id!!,
                    it.spotName,
                    it.spotThumbnailImageLink,
                    it.spotAddress,
                    it.spotInfo,
                    it.spotLevel,
                    it.latitude,
                    it.longitude
                )
            }
        )
    }

    @GetMapping("/api/spots/{spotId}")
    fun getSpot(
        @PathVariable("spotId") spotId: Long
    ): ResponseEntity<SpotResponse> {
        val spot = spotService.getSpot(spotId)
        return ResponseEntity.ok(
            SpotResponse(
                spot.id!!,
                spot.spotName,
                spot.spotThumbnailImageLink,
                spot.spotAddress,
                spot.spotInfo,
                spot.spotLevel,
                spot.latitude,
                spot.longitude
            )
        )
    }
}