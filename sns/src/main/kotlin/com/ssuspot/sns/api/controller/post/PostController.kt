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
    @GetMapping("/api/posts/{postId}")
    fun getSpecificPost(
        @PathVariable postId: Long
    ): ResponseEntity<PostResponse>{
        val post = postService.getPostById(postId)
        return ResponseEntity.ok().body(
            post.toDto()
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
                it.toDto()
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
                it.toDto()
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
                it.toDto()
            }
        )
    }

    @GetMapping("/api/tags")
    fun getPostsByTagName(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("tagName") tagName: String
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.findPostsByTagName(GetTagRequestDto(page, size, tagName))
        return ResponseEntity.ok(
            posts.map {
                it.toDto()
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
                request.imageUrl,
                request.tags,
                request.spotId
            )
        )
        return ResponseEntity.ok().body(
            savedPost.toDto()
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
            updatedPost.toDto()
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