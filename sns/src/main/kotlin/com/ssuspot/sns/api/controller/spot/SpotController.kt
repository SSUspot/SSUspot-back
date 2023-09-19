package com.ssuspot.sns.api.controller.spot

import com.ssuspot.sns.application.dto.spot.CreateSpotDto
import com.ssuspot.sns.api.request.spot.CreateSpotRequest
import com.ssuspot.sns.application.service.spot.SpotService
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