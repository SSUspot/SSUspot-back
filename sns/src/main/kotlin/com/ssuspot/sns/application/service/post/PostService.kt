package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.CreatePostDto
import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.application.dto.post.GetMyPostsDto
import com.ssuspot.sns.application.dto.post.GetUserPostsDto
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.infrastructure.repository.post.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val spotService: SpotService,
    private val userService: UserService
) {
    fun createPost(createPostDto: CreatePostDto): PostResponseDto {
        val post = createPostDto.toEntity(
            spotService.findValidSpot(createPostDto.spotId),
            userService.findValidUserByEmail(createPostDto.email)
        )
        val savedPost = postRepository.save(post)
        return savedPost.toDto()
    }

    fun getMyPosts(getPostsRequest: GetMyPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = postRepository.findPostsByUserId(user.id!!, toPageable(getPostsRequest.page, getPostsRequest.size))
        return posts.content.map { it.toDto() }
    }

    fun getPostsBySpotId(spotId: Long, page: Int, size: Int): List<PostResponseDto> {
        val posts = postRepository.findPostsBySpotId(spotId, toPageable(page, size))
        return posts.content.map { it.toDto() }
    }

    fun getPostsByUserId(getPostsRequest: GetUserPostsDto): List<PostResponseDto> {
        val posts = postRepository.findPostsByUserId(getPostsRequest.userId, toPageable(getPostsRequest.page, getPostsRequest.size))
        return posts.content.map { it.toDto() }
    }

    private fun CreatePostDto.toEntity(spot: Spot, user: User): Post =
        Post(title = title, user = user, content = content, imageUrls = imageUrls, spot = spot)

    private fun Post.toDto(): PostResponseDto =
        PostResponseDto(id = id!!, title = title, content = content, email = user.email, imageUrls = imageUrls, spotId = spot.id!!)

    private fun toPageable(page: Int, size: Int) = PageRequest.of(page - 1, size)
}
