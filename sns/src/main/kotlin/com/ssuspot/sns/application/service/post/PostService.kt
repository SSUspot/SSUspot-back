package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.infrastructure.repository.post.PostRepository
import com.ssuspot.sns.infrastructure.repository.spot.SpotRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val spotRepository: SpotRepository
) {
    fun createPost(
            createPostDto: CreatePostDto
    ) {
        //spot 찾음
        val spot = spotRepository.findById(createPostDto.spotId)
                .orElseThrow { throw Exception("spot not found") }


        postRepository.save(
                Post(
                        title = createPostDto.title,
                        content = createPostDto.content,
                        imageUrls = createPostDto.imageUrls,
                        spot = spot
                )
        )
    }
}