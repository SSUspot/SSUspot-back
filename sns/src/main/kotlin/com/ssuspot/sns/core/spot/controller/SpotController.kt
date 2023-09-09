package com.ssuspot.sns.core.spot.controller

import com.ssuspot.sns.core.spot.model.dto.CreateSpotDto
import com.ssuspot.sns.core.spot.request.CreateSpotRequest
import com.ssuspot.sns.core.spot.service.SpotService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/spot")
class SpotController(
        val spotService: SpotService
) {
    //create spot
    @PostMapping
    fun createSpot(
            @RequestBody createSpotRequest: CreateSpotRequest
    ){
        spotService.createSpot(
                CreateSpotDto(
                        createSpotRequest.spotName,
                        createSpotRequest.spotThumbnailImageLink,
                        createSpotRequest.spotLevel,
                        createSpotRequest.latitude,
                        createSpotRequest.longitude
                )
        )
    }
    //get spot list
}