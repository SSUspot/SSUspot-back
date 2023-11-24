package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.LikeDto
import com.ssuspot.sns.application.dto.post.LikeResponseDto
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.alarm.event.LikeEvent
import com.ssuspot.sns.domain.model.post.entity.PostLike
import com.ssuspot.sns.infrastructure.repository.post.PostLikeRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostLikeService(
    private val postLikeRepository: PostLikeRepository,
    private val postService: PostService,
    private val userService: UserService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun likePost(requestDto: LikeDto): LikeResponseDto {
        val post = postService.findValidPostById(requestDto.postId)
        val user = userService.findValidUserByEmail(requestDto.userEmail)
        val like: PostLike
        var pressed = "liked"
        // 이미 좋아요를 눌렀는지 확인
        val liked = postLikeRepository.findByPostAndUser(post, user)
        if (liked != null) {
            like = liked
            pressed = "unliked"
            postLikeRepository.deleteByPostAndUser(post, user)
        } else {
            like = postLikeRepository.save(
                PostLike(
                    user,
                    post
                )
            )
            val likeEvent = LikeEvent(
                postUserId = post.user.id!!,
                postId = post.id!!,
                likeUserId = user.id!!
            )
            applicationEventPublisher.publishEvent(likeEvent)
        }

        return like.toDto(pressed)
    }

    private fun PostLike.toDto(pressed: String): LikeResponseDto {
        return LikeResponseDto(
            this.id!!,
            this.post.id!!,
            this.user.nickname,
            pressed
        )
    }
}