package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.infrastructure.repository.post.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val spotService: SpotService,
    private val userService: UserService
) {
    fun createPost(
        createPostDto: CreatePostDto
    ) {
        //spot 찾음
        val spot = spotService.findValidSpot(createPostDto.spotId)
        val user = userService.findValidUserByEmail(createPostDto.email)

        postRepository.save(
            Post(
                title = createPostDto.title,
                user = user,
                content = createPostDto.content,
                imageUrls = createPostDto.imageUrls,
                spot = spot
            )
        )
    }
}