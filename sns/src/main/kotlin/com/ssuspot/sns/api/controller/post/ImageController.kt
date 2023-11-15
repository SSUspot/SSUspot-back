package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.ImageResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.ImageDto
import com.ssuspot.sns.application.service.post.ImageService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping("/api/images")
    fun uploadImage(
        @RequestParam("image") image: MultipartFile,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<ImageResponse> {
        val savedImage = imageService.uploadSingleImage(image, authInfo.email)
        return ResponseEntity.ok(
            ImageResponse(
                savedImage.imageId,
                savedImage.userId,
                savedImage.imageUrl
            )
        )
    }
}