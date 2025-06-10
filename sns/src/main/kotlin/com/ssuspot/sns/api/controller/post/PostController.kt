package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.request.post.UpdatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.*
import com.ssuspot.sns.application.service.post.PostService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Post", description = "게시물 관리 API - 게시물 생성, 조회, 수정, 삭제 및 태그 기반 검색 기능을 제공합니다.")
class PostController(
    private val postService: PostService
) {
    @GetMapping("/api/posts/recommend")
    @Operation(
        summary = "추천 게시물 조회",
        description = "사용자 맞춤 추천 게시물 목록을 조회합니다. 팔로우하는 사용자들의 인기 게시물을 기반으로 추천됩니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "추천 게시물 조회 성공"
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
        )
    ])
    fun getRecommendPosts(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getRecommendedPosts(GetRecommendedPostsDto(authInfo.email, page, size))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }



    @GetMapping("/api/posts")
    @Operation(
        summary = "팔로잉 사용자의 게시물 조회",
        description = "현재 사용자가 팔로우하는 사용자들의 게시물 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getFollowingPosts(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getFollowingPosts(GetFollowingPostsDto(authInfo.email, page, size))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }


    @GetMapping("/api/posts/{postId}")
    @Operation(
        summary = "특정 게시물 상세 조회",
        description = "게시물 ID로 특정 게시물의 상세 정보를 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
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
    fun getSpecificPost(
        @Parameter(description = "조회할 게시물 ID", required = true, example = "1")
        @PathVariable postId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<PostResponse> {
        val post = postService.getPostById(
            SpecificPostRequestDto(
                postId,
                authInfo.email
            )
        )
        return ResponseEntity.ok().body(
            post.toResponseDto()
        )
    }

    @GetMapping("/api/posts/users/me")
    @Operation(
        summary = "내 게시물 목록 조회",
        description = "현재 로그인한 사용자의 게시물 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getMyPosts(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getMyPosts(GetMyPostsDto(page, size, authInfo.email))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }

    @GetMapping("/api/posts/users/{userId}")
    @Operation(
        summary = "특정 사용자의 게시물 목록 조회",
        description = "특정 사용자가 작성한 게시물 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getPostsByUserId(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
        @Parameter(description = "정렬 기준 (postId, createdAt, likeCount)", example = "postId")
        @RequestParam("sort", defaultValue = "postId") sort: String,
        @Parameter(description = "조회할 사용자 ID", required = true, example = "1")
        @PathVariable("userId") userId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsByUserId(GetUserPostsDto(page, size, sort, userId, authInfo.email))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }

    @GetMapping("/api/posts/spots/{spotId}")
    @Operation(
        summary = "특정 스팟의 게시물 목록 조회",
        description = "특정 스팟(장소)에 대한 게시물 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "스팟을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getPostsBySpotId(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
        @Parameter(description = "정렬 기준 (postId, createdAt, likeCount)", example = "postId")
        @RequestParam("sort", defaultValue = "postId") sort: String,
        @Parameter(description = "조회할 스팟 ID", required = true, example = "1")
        @PathVariable("spotId") spotId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsBySpotId(
            GetPostsBySpotIdDto(
                spotId,
                authInfo.email,
                page,
                size,
            )
        )
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }

    @GetMapping("/api/tags")
    @Operation(
        summary = "태그로 게시물 검색",
        description = "특정 태그가 포함된 게시물 목록을 조회합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 목록 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "태그를 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun getPostsByTagName(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam("page", defaultValue = "1") page: Int,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam("size", defaultValue = "10") size: Int,
        @Parameter(description = "검색할 태그 이름", required = true, example = "카페")
        @RequestParam("tagName") tagName: String,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.findPostsByTagName(GetTagRequestDto(page, size, tagName, authInfo.email))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }


    @PostMapping("/api/posts")
    @Operation(
        summary = "게시물 생성",
        description = "새로운 게시물을 생성합니다. 이미지, 태그, 스팟 정보를 포함할 수 있습니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 생성 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (필수 필드 누락, 유효하지 않은 데이터)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "스팟을 찾을 수 없음 (스팟 ID를 제공한 경우)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun createPost(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "게시물 생성 요청 데이터",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CreatePostRequest::class))]
        )
        @RequestBody request: CreatePostRequest,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<PostResponse> {
        val savedPost = postService.createPost(
            CreatePostRequestDto(
                request.title,
                request.content,
                authInfo.email,
                request.imageUrls,
                request.tags,
                request.spotId
            )
        )
        return ResponseEntity.ok().body(
            savedPost.toResponseDto()
        )
    }

    @PatchMapping("/api/posts/{postId}")
    @Operation(
        summary = "게시물 수정",
        description = "기존 게시물의 제목, 내용, 태그를 수정합니다. 본인이 작성한 게시물만 수정 가능합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 수정 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효하지 않은 데이터)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인의 게시물이 아님)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "게시물을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun updatePost(
        @Parameter(description = "수정할 게시물 ID", required = true, example = "1")
        @PathVariable postId: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "게시물 수정 요청 데이터",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UpdatePostRequest::class))]
        )
        @RequestBody request: UpdatePostRequest,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ): ResponseEntity<PostResponse> {
        val updatedPost = postService.updatePost(
            UpdatePostRequestDto(
                postId,
                request.title,
                request.content,
                authInfo.email,
                request.tags
            )
        )
        return ResponseEntity.ok().body(
            updatedPost.toResponseDto()
        )
    }

    @DeleteMapping("/api/posts/{postId}")
    @Operation(
        summary = "게시물 삭제",
        description = "게시물을 삭제합니다. 본인이 작성한 게시물만 삭제 가능합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게시물 삭제 성공"
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한 없음 (본인의 게시물이 아님)",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        ),
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "게시물을 찾을 수 없음",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
        )
    ])
    fun deletePost(
        @Parameter(description = "삭제할 게시물 ID", required = true, example = "1")
        @PathVariable postId: Long,
        @Parameter(hidden = true) @Auth authInfo: AuthInfo
    ) {
        postService.deletePost(
            SpecificPostRequestDto(
                postId,
                authInfo.email
            )
        )
    }
}