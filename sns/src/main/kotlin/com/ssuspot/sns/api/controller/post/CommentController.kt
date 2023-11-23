package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.request.post.CreateCommentRequest
import com.ssuspot.sns.api.request.post.UpdateCommentRequest
import com.ssuspot.sns.api.response.post.CommentResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.CreateCommentDto
import com.ssuspot.sns.application.dto.post.GetCommentDto
import com.ssuspot.sns.application.service.post.CommentService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
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
                    nickname = it.nickname,
                    content = it.content,
                )
            }
        )
    }

    @GetMapping("/api/comments/{commentId}")
    fun getCommentById(
        @PathVariable commentId: Long
    ): ResponseEntity<CommentResponse> {
        val comment = commentService.getCommentById(commentId)
        return ResponseEntity.ok(
            CommentResponse(
                id = comment.id,
                postId = comment.postId,
                nickname = comment.nickname,
                content = comment.content,
            )
        )
    }

    @PostMapping("/api/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody createCommentRequest: CreateCommentRequest,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<CommentResponse> {
        val savedComment = commentService.createComment(
            CreateCommentDto(
                postId = postId,
                userEmail = authInfo.email,
                content = createCommentRequest.content,
            )
        )
        return ResponseEntity.ok().body(
            CommentResponse(
                id = savedComment.id,
                postId = savedComment.postId,
                nickname = savedComment.nickname,
                content = savedComment.content,
            )
        )
    }

    //modify comment
    @PutMapping("/api/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<CommentResponse> {
        val updatedComment = commentService.updateComment(commentId, updateCommentRequest.content, authInfo.email)
        return ResponseEntity.ok().body(
            CommentResponse(
                id = updatedComment.id,
                postId = updatedComment.postId,
                nickname = updatedComment.nickname,
                content = updatedComment.content,
            )
        )
    }

    //delete comment
    @DeleteMapping("/api/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<Unit> {
        commentService.deleteComment(commentId, authInfo.email)
        return ResponseEntity.ok().build()
    }
}