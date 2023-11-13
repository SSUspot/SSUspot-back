package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.GetMyPostsDto
import com.ssuspot.sns.application.dto.post.GetUserPostsDto
import com.ssuspot.sns.application.service.post.PostService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
) {
    @GetMapping("/api/posts/{postId}")
    fun getSpecificPost(
        @PathVariable postId: Long
    ): ResponseEntity<PostResponse>{
        val post = postService.getPostById(postId)
        return ResponseEntity.ok().body(
            PostResponse(
                post.id,
                post.title,
                post.content,
                post.nickname,
                post.imageUrls,
                post.spotId
            )
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
                PostResponse(
                    it.id,
                    it.title,
                    it.content,
                    it.nickname,
                    it.imageUrls,
                    it.spotId
                )
            }
        )
    }

    // Get specific user's posts
    @GetMapping("/api/posts/users/{userId}")
    fun getPostsByUserId(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort", defaultValue = "postId") sort: String,
        @PathVariable("userId") userId: Long
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsByUserId(GetUserPostsDto(page, size, sort, userId))
        return ResponseEntity.ok(
            posts.map {
                PostResponse(
                    it.id,
                    it.title,
                    it.content,
                    it.nickname,
                    it.imageUrls,
                    it.spotId
                )
            }
        )
    }

    // Get specific spot's posts
    @GetMapping("/api/posts/spots/{spotId}")
    fun getPostsBySpotId(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort", defaultValue = "postId") sort: String,
        @PathVariable("spotId") spotId: Long
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsBySpotId(spotId, page, size)
        return ResponseEntity.ok(
            posts.map {
                PostResponse(
                    it.id,
                    it.title,
                    it.content,
                    it.nickname,
                    it.imageUrls,
                    it.spotId
                )
            }
        )
    }


    @PostMapping("/api/posts")
    fun createPost(
        @RequestBody request: CreatePostRequest,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<PostResponse> {
        val savedPost = postService.createPost(
            CreatePostDto(
                request.title,
                request.content,
                authInfo.email,
                request.imageUrls,
                request.spotId
            )
        )
        return ResponseEntity.ok().body(
            PostResponse(
                savedPost.id,
                savedPost.title,
                savedPost.content,
                savedPost.nickname,
                savedPost.imageUrls,
                savedPost.spotId
            )
        )
    }
}