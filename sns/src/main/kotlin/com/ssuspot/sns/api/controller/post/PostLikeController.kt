package com.ssuspot.sns.api.controller.post

import com.ssuspot.sns.api.response.post.LikeResponse
import com.ssuspot.sns.application.annotation.Auth
import com.ssuspot.sns.application.dto.post.LikeDto
import com.ssuspot.sns.application.service.post.PostLikeService
import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.http.ResponseEntity
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
        @Auth authInfo: AuthInfo
    ):ResponseEntity<LikeResponse> {
        val like = postLikeService.likePost(
            LikeDto(
                postId,
                authInfo.email,
            )
        )
        return ResponseEntity.ok(
            LikeResponse(
                like.id,
                like.postId,
                like.userNickname,
                like.pressed
            )
        )
    }
}
