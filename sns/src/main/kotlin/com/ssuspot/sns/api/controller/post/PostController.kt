package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.request.post.UpdatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.*
import com.ssuspot.sns.application.service.post.PostService
import com.ssuspot.sns.infrastructure.security.AuthInfo
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
class PostController(
    private val postService: PostService
) {
    @GetMapping("/api/posts/recommend")
    fun getRecommendPosts(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @Auth authInfo: AuthInfo
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