package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.ImageResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.ImageDto
import com.ssuspot.sns.application.service.post.ImageService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import com.ssuspot.sns.api.response.common.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "Image", description = "이미지 업로드 API - 게시물에 사용할 이미지를 업로드하고 관리합니다.")
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping("/api/images")
    @Operation(
        summary = "이미지 업로드",
        description = "여러 개의 이미지를 동시에 업로드합니다. 업로드된 이미지는 게시물 작성 시 사용할 수 있습니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "이미지 업로드 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ImageResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (지원하지 않는 파일 형식, 파일 크기 초과 등)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "413",
            description = "파일 크기가 너무 큼",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류 (스토리지 오류 등)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun uploadImage(
        @Parameter(
            description = "업로드할 이미지 파일 목록 (JPEG, PNG, GIF 지원)",
            required = true
        )
        @RequestParam("image") images: List<MultipartFile>,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<ImageResponse>> {
        val savedImages = imageService.uploadMultipleFiles(images, authInfo.email)
        return ResponseEntity.ok(
            savedImages.map {
                ImageResponse(
                    imageId = it.imageId,
                    userId = it.userId,
                    imageUrl = it.imageUrl
                )
            }
        )
    }
}