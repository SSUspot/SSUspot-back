package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.LikeResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.LikeDto
import com.ssuspot.sns.application.service.post.PostLikeService
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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Like", description = "좋아요 관리 API - 게시물에 대한 좋아요 기능을 제공합니다. 좋아요를 다시 누르면 좋아요가 취소됩니다.")
class PostLikeController(
    private val postLikeService: PostLikeService
) {
    @PostMapping("/api/posts/{postId}/likes")
    @Operation(
        summary = "게시물 좋아요/취소",
        description = "게시물에 좋아요를 누르거나 취소합니다. 이미 좋아요를 누른 게시물에 다시 요청하면 좋아요가 취소됩니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "좋아요 처리 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = LikeResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "게시물을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun likePost(
        @Parameter(description = "좋아요를 누를 게시물 ID", required = true, example = "1")
        @PathVariable("postId") postId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ):ResponseEntity<LikeResponse> {
        val like = postLikeService.likePost(
            LikeDto(
                postId,
                authInfo.email,
            )
        )
        return ResponseEntity.ok(
            LikeResponse(
                like.id,
                like.postId,
                like.userNickname,
                like.pressed
            )
        )
    }
}
