package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreateCommentRequest
import com.ssuspot.sns.api.request.post.UpdateCommentRequest
import com.ssuspot.sns.api.response.post.CommentResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.CreateCommentDto
import com.ssuspot.sns.application.dto.post.GetCommentDto
import com.ssuspot.sns.application.service.post.CommentService
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
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Comment", description = "댓글 관리 API - 게시물에 대한 댓글 생성, 조회, 수정, 삭제 기능을 제공합니다.")
class CommentController(
    private val commentService: CommentService,
) {
    @GetMapping("/api/posts/{postId}/comments")
    @Operation(
        summary = "게시물의 댓글 목록 조회",
        description = "특정 게시물에 달린 댓글 목록을 페이지네이션하여 조회합니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "댓글 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CommentResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "게시물을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getCommentsByPostId(
        @Parameter(description = "댓글을 조회할 게시물 ID", required = true, example = "1")
        @PathVariable postId: Long,
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
    ): ResponseEntity<List<CommentResponse>> {
        val comments = commentService.getCommentsByPostId(
            GetCommentDto(
                postId = postId,
                page = page,
                size = size
            )
        )
        return ResponseEntity.ok(
            comments.map {
                it.toResponseDto()
            }
        )
    }

    @GetMapping("/api/comments/{commentId}")
    @Operation(
        summary = "특정 댓글 조회",
        description = "댓글 ID로 특정 댓글의 상세 정보를 조회합니다."
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "댓글 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CommentResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "댓글을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getCommentById(
        @Parameter(description = "조회할 댓글 ID", required = true, example = "1")
        @PathVariable commentId: Long
    ): ResponseEntity<CommentResponse> {
        val comment = commentService.getCommentById(commentId)
        return ResponseEntity.ok(
            comment.toResponseDto()
        )
    }

    @PostMapping("/api/posts/{postId}/comments")
    @Operation(
        summary = "댓글 작성",
        description = "특정 게시물에 새로운 댓글을 작성합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "댓글 작성 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CommentResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (비어있는 내용 등)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
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
    fun createComment(
        @Parameter(description = "댓글을 작성할 게시물 ID", required = true, example = "1")
        @PathVariable postId: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 작성 요청 데이터",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CreateCommentRequest::class))]
        )
        @RequestBody createCommentRequest: CreateCommentRequest,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<CommentResponse> {
        val savedComment = commentService.createComment(
            CreateCommentDto(
                postId = postId,
                userEmail = authInfo.email,
                content = createCommentRequest.content,
            )
        )
        return ResponseEntity.ok().body(
            savedComment.toResponseDto()
        )
    }

    @PutMapping("/api/comments/{commentId}")
    @Operation(
        summary = "댓글 수정",
        description = "작성한 댓글의 내용을 수정합니다. 본인이 작성한 댓글만 수정 가능합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "댓글 수정 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CommentResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (비어있는 내용 등)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인의 댓글이 아님)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "댓글을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun updateComment(
        @Parameter(description = "수정할 댓글 ID", required = true, example = "1")
        @PathVariable commentId: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 수정 요청 데이터",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UpdateCommentRequest::class))]
        )
        @RequestBody updateCommentRequest: UpdateCommentRequest,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<CommentResponse> {
        val updatedComment = commentService.updateComment(commentId, updateCommentRequest.content, authInfo.email)
        return ResponseEntity.ok().body(
            updatedComment.toResponseDto()
        )
    }

    @DeleteMapping("/api/comments/{commentId}")
    @Operation(
        summary = "댓글 삭제",
        description = "작성한 댓글을 삭제합니다. 본인이 작성한 댓글만 삭제 가능합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "댓글 삭제 성공"
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인의 댓글이 아님)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "댓글을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun deleteComment(
        @Parameter(description = "삭제할 댓글 ID", required = true, example = "1")
        @PathVariable commentId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<Unit> {
        commentService.deleteComment(commentId, authInfo.email)
        return ResponseEntity.ok().build()
    }
}