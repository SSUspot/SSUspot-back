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
@Tag(name = "Post", description = "게시물 관리 API")
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
    fun getFollowingPosts(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getFollowingPosts(GetFollowingPostsDto(authInfo.email, page, size))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }


    @GetMapping("/api/posts/{postId}")
    fun getSpecificPost(
        @PathVariable postId: Long,
        @Auth authInfo: AuthInfo
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
    fun getMyPosts(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getMyPosts(GetMyPostsDto(page, size, authInfo.email))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }

    @GetMapping("/api/posts/users/{userId}")
    fun getPostsByUserId(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort", defaultValue = "postId") sort: String,
        @PathVariable("userId") userId: Long,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsByUserId(GetUserPostsDto(page, size, sort, userId, authInfo.email))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }

    @GetMapping("/api/posts/spots/{spotId}")
    fun getPostsBySpotId(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort", defaultValue = "postId") sort: String,
        @PathVariable("spotId") spotId: Long,
        @Auth authInfo: AuthInfo
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
    fun getPostsByTagName(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("tagName") tagName: String,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.findPostsByTagName(GetTagRequestDto(page, size, tagName, authInfo.email))
        return ResponseEntity.ok(
            posts.map {
                it.toResponseDto()
            }
        )
    }


    @PostMapping("/api/posts")
    fun createPost(
        @RequestBody request: CreatePostRequest,
        @Auth authInfo: AuthInfo
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
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody request: UpdatePostRequest,
        @Auth authInfo: AuthInfo
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
    fun deletePost(
        @PathVariable postId: Long,
        @Auth authInfo: AuthInfo
    ) {
        postService.deletePost(
            SpecificPostRequestDto(
                postId,
                authInfo.email
            )
        )
    }
}