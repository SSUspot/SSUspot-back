package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.application.dto.post.CreatePostRequestDto
import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.request.post.UpdatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.GetMyPostsDto
import com.ssuspot.sns.application.dto.post.GetUserPostsDto
import com.ssuspot.sns.application.dto.post.UpdatePostRequestDto
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
                post.tags,
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
                    it.tags,
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
                    it.tags,
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
                    it.tags,
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
            PostResponse(
                savedPost.id,
                savedPost.title,
                savedPost.content,
                savedPost.nickname,
                savedPost.imageUrls,
                savedPost.tags,
                savedPost.spotId
            )
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
                request.title,
                request.content,
                authInfo.email,
                request.tags
            ),
            postId
        )
        return ResponseEntity.ok().body(
            PostResponse(
                updatedPost.id,
                updatedPost.title,
                updatedPost.content,
                updatedPost.nickname,
                updatedPost.imageUrls,
                updatedPost.tags,
                updatedPost.spotId
            )
        )
    }

    @DeleteMapping("/api/posts/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @Auth authInfo: AuthInfo
    ) {
        postService.deletePost(postId, authInfo.email)
    }
}