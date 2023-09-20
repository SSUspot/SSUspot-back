package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.api.response.post.PostResponse
import com.ssuspot.sns.application.dto.post.GetPostsDto
import com.ssuspot.sns.application.service.post.PostService
import com.ssuspot.sns.infrastructure.security.UserPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
) {
    @GetMapping("/api/posts")
    fun getMyPosts(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @AuthenticationPrincipal userDetails: UserPrincipal
    ): ResponseEntity<List<PostResponse>> {
        val posts = postService.getPostsByUserId(GetPostsDto(page, size, userDetails.username))
        return ResponseEntity.ok(
            posts.content.map {
                PostResponse(
                    it.id!!,
                    it.title,
                    it.content,
                    it.user.email,
                    it.imageUrls,
                    it.spot.id!!
                )
            }
        )
    }


    @PostMapping("/api/post")
    fun createPost(
        @RequestBody request: CreatePostRequest,
        @AuthenticationPrincipal userDetails: UserPrincipal
    ): ResponseEntity<PostResponse> {
        val savedPost = postService.createPost(
            CreatePostDto(
                request.title,
                request.content,
                userDetails.username,
                request.imageUrls,
                request.spotId
            )
        )
        return ResponseEntity.ok().body(
            PostResponse(
                savedPost.postId,
                savedPost.title,
                savedPost.content,
                savedPost.email,
                savedPost.imageUrls,
                savedPost.spotId
            )
        )
    }
}