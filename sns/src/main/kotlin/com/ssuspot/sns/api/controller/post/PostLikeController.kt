package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.LikeResponse
import com.ssuspot.sns.application.dto.post.LikeDto
import com.ssuspot.sns.application.service.post.PostLikeService
import com.ssuspot.sns.infrastructure.security.UserPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostLikeController(
    private val postLikeService: PostLikeService
) {
    @PostMapping("/api/posts/{postId}/likes")
    fun likePost(
        @PathVariable("postId") postId: Long,
        @AuthenticationPrincipal userDetails: UserPrincipal
    ):ResponseEntity<LikeResponse> {
        val like = postLikeService.likePost(
            LikeDto(
                postId,
                userDetails.username
            )
        )
        return ResponseEntity.ok(
            LikeResponse(
                like.id,
                like.postId,
                like.userNickname
            )
        )
    }

}