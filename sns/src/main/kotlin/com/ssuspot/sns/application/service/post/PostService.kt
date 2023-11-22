package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.*
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.infrastructure.repository.post.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val spotService: SpotService,
    private val userService: UserService,
    private val tagService: TagService
) {
    @Transactional
    fun createPost(createPostDto: CreatePostDto): PostResponseDto {
        val post = createPostDto.toEntity(
            spotService.findValidSpot(createPostDto.spotId),
            userService.findValidUserByEmail(createPostDto.userEmail),
        )
        var savedPost = postRepository.save(post)
        val postTags = createTags(createPostDto.tags, savedPost)
        savedPost.postTags = postTags as MutableList<PostTag>
        postRepository.save(savedPost)
        return savedPost.toDto()
    }

    @Transactional(readOnly = true)
    fun getMyPosts(getPostsRequest: GetMyPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = postRepository.findPostsByUserId(
            user.id!!,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content.map { it.toDto() }
    }

    @Transactional(readOnly = true)
    fun getPostById(postId: Long): PostResponseDto {
        val post = postRepository.findPostById(postId) ?: throw PostNotFoundException()
        return post.toDto()
    }

    @Transactional(readOnly = true)
    fun getPostsBySpotId(spotId: Long, page: Int, size: Int): List<PostResponseDto> {
        val posts = postRepository.findPostsBySpotId(spotId, toPageableLatestSort(page, size))
        return posts.content.map { it.toDto() }
    }

    @Transactional(readOnly = true)
    fun getPostsByUserId(getPostsRequest: GetUserPostsDto): List<PostResponseDto> {
        val posts = postRepository.findPostsByUserId(
            getPostsRequest.userId,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content.map { it.toDto() }
    }

    @Transactional(readOnly = true)
    fun findValidPostById(postId: Long): Post {
        return postRepository.findPostById(postId) ?: throw PostNotFoundException()
    }

    private fun createTags(tags: List<String>, post: Post): List<PostTag> {
        val createdTags = mutableListOf<PostTag>()
        tags.forEach{
            // 1. tags를 순회하면서 tag를 찾는다. 없으면 생성
            val tag = tagService.findValidTagByName(it)
            // 2. postTag를 생성한다.
            val postTag = PostTag(
                post = post,
                tag = tag
            )
            // 3. postTag를 저장한다.
            createdTags.add(postTag)
        }
        return createdTags
    }

    private fun CreatePostDto.toEntity(spot: Spot, user: User): Post =
        Post(
            title = title,
            user = user,
            content = content,
            imageUrls = imageUrls,
            spot = spot
        )

    private fun Post.toDto(): PostResponseDto =
        PostResponseDto(
            id = id!!,
            title = title,
            content = content,
            nickname = user.nickname,
            imageUrls = imageUrls,
            spotId = spot.id!!
        )

    private fun toPageableLatestSort(page: Int, size: Int) = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id")
}
