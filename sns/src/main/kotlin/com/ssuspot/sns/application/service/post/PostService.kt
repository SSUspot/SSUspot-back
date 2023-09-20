package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.application.dto.post.CreatePostResponseDto
import com.ssuspot.sns.application.dto.post.GetPostsDto
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.infrastructure.repository.post.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val spotService: SpotService,
    private val userService: UserService
) {
    fun createPost(
        createPostDto: CreatePostDto
    ): CreatePostResponseDto {
        val spot = spotService.findValidSpot(createPostDto.spotId)
        val user = userService.findValidUserByEmail(createPostDto.email)

        val savedPost = postRepository.save(
            Post(
                title = createPostDto.title,
                user = user,
                content = createPostDto.content,
                imageUrls = createPostDto.imageUrls,
                spot = spot
            )
        )
        return CreatePostResponseDto(
            postId = savedPost.id!!,
            title = savedPost.title,
            content = savedPost.content,
            email = savedPost.user.email,
            imageUrls = savedPost.imageUrls,
            spotId = savedPost.spot.id!!
        )
    }

    fun getPostsByUserId(getPostsRequest: GetPostsDto): Page<Post> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val pageable = PageRequest.of(getPostsRequest.page - 1, getPostsRequest.size)
        return postRepository.findPostsByUserUserId(user.id!!, pageable)
    }
}