package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreateCommentRequest
import com.ssuspot.sns.api.response.post.CommentResponse
import com.ssuspot.sns.application.dto.post.CreateCommentDto
import com.ssuspot.sns.application.dto.post.GetCommentDto
import com.ssuspot.sns.application.service.post.CommentService
import com.ssuspot.sns.infrastructure.security.UserPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @GetMapping("/api/posts/{postId}/comments")
    fun getCommentsByPostId(
        @PathVariable postId: Long,
        @RequestParam("page", defaultValue = "1") page: Int,
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
                CommentResponse(
                    id = it.id,
                    postId = it.postId,
                    userEmail = it.userEmail,
                    content = it.content,
                )
            }
        )
    }

    @PostMapping("/api/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest,
        @AuthenticationPrincipal userDetails: UserPrincipal
    ): ResponseEntity<CommentResponse> {
        val savedComment = commentService.createComment(
            CreateCommentDto(
                postId = postId,
                userEmail = userDetails.username,
                content = createCommentRequest.content,
            )
        )
        return ResponseEntity.ok().body(
            CommentResponse(
                id = savedComment.id,
                postId = savedComment.postId,
                userEmail = savedComment.userEmail,
                content = savedComment.content,
            )
        )
    }
}