package com.ssuspot.sns.api.controller.spot

import com.ssuspot.sns.application.dto.spot.CreateSpotDto
import com.ssuspot.sns.api.request.spot.CreateSpotRequest
import com.ssuspot.sns.api.response.spot.SpotResponse
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.api.response.common.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Spot", description = "스팟(장소) 관리 API - 장소 생성, 조회 및 관리 기능을 제공합니다. 스팟은 게시물이 연결될 수 있는 특정 위치를 나타냅니다.")
class SpotController(
    val spotService: SpotService
) {
    @PostMapping("/api/spots")
    @Operation(
        summary = "스팟 생성",
        description = "새로운 스팟(장소)를 생성합니다. 스팟은 지도상의 특정 위치를 나타내며, 게시물과 연결될 수 있습니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "스팟 생성 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SpotResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (필수 필드 누락, 유효하지 않은 좌표 등)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun createSpot(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "스팟 생성 요청 데이터",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CreateSpotRequest::class))]
        )
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

    @GetMapping("/api/spots")
    @Operation(
        summary = "전체 스팟 목록 조회",
        description = "등록된 모든 스팟의 목록을 조회합니다. 페이지네이션이 없으므로 대량의 데이터가 있을 경우 주의가 필요합니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "스팟 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SpotResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getSpotList(): ResponseEntity<List<SpotResponse>> {
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
    @Operation(
        summary = "특정 스팟 상세 조회",
        description = "스팟 ID로 특정 스팟의 상세 정보를 조회합니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "스팟 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SpotResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "스팟을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getSpot(
        @Parameter(description = "조회할 스팟 ID", required = true, example = "1")
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