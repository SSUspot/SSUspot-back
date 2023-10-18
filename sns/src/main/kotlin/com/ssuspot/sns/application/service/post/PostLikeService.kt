package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.LikeDto
import com.ssuspot.sns.application.dto.post.LikeResponseDto
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.post.entity.PostLike
import com.ssuspot.sns.infrastructure.repository.post.PostLikeRepository
import org.springframework.stereotype.Service

@Service
class PostLikeService(
    private val postLikeRepository: PostLikeRepository,
    private val postService: PostService,
    private val userService: UserService
) {
    fun likePost(requestDto: LikeDto): LikeResponseDto {
        val post = postService.findValidPostById(requestDto.postId)
        val user = userService.findValidUserByEmail(requestDto.userEmail)
        val savedLike = postLikeRepository.save(
            PostLike(
                user,
                post
            )
        )
        return savedLike.toDto()
    }

    private fun PostLike.toDto(): LikeResponseDto {
        return LikeResponseDto(
            this.id!!,
            this.post.id!!,
            this.user.nickname
        )
    }
}