package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.api.request.post.CreatePostRequest
import com.ssuspot.sns.application.service.post.PostService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun createPost(
        @RequestBody request: CreatePostRequest,
        @AuthenticationPrincipal userId:Long
    ){
        val savedPost = postService.createPost(
                CreatePostDto(
                        request.title,
                        request.content,
                        request.imageUrls,
                        request.spotId
                )
        )
    }
}