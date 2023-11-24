package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.*
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.exceptions.post.AccessPostWithNoAuthException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.spot.entity.Spot
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val customPostRepository: CustomPostRepository,
    private val spotService: SpotService,
    private val userService: UserService,
    private val tagService: TagService
) {
    @Transactional
    fun createPost(createPostRequestDto: CreatePostRequestDto): PostResponseDto {
        val post = createPostRequestDto.toEntity(
            spotService.findValidSpot(createPostRequestDto.spotId),
            userService.findValidUserByEmail(createPostRequestDto.userEmail),
        )
        var savedPost = customPostRepository.save(post)
        val postTags = createTags(createPostRequestDto.tags, savedPost)
        savedPost.postTags = postTags as MutableList<PostTag>
        savedPost = customPostRepository.save(savedPost)
        return savedPost.toDto()
    }

    @Transactional
    fun updatePost(updatePostRequestDto: UpdatePostRequestDto, postId: Long): PostResponseDto {
        val post = findValidPostById(postId)
        val user = userService.findValidUserByEmail(updatePostRequestDto.email)
        // check if user is the owner of the post
        checkPostOwner(post, user)
        updatePostArticle(post, updatePostRequestDto)
        return customPostRepository.save(post).toDto()
    }

    @Transactional(readOnly = true)
    fun getMyPosts(getPostsRequest: GetMyPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = customPostRepository.findPostsByUserId(
            user.id!!,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content.map { it.toDto() }
    }

    @Transactional(readOnly = true)
    fun deletePost(postId: Long, email: String) {
        val post = findValidPostById(postId)
        val user = userService.findValidUserByEmail(email)
        checkPostOwner(post, user)
        customPostRepository.delete(post)
    }


    fun getPostById(postId: Long): PostResponseDto {
        val post = customPostRepository.findPostById(postId) ?: throw PostNotFoundException()
        return post.toDto()
    }
    @Transactional(readOnly = true)
    fun getPostsBySpotId(spotId: Long, page: Int, size: Int): List<PostResponseDto> {
        val posts = customPostRepository.findPostsBySpotId(spotId, toPageableLatestSort(page, size))
        return posts.content.map { it.toDto() }
    }

    @Transactional(readOnly=true)
    fun findPostsByTagName(request: GetTagRequestDto): List<PostResponseDto> {
        val posts = customPostRepository.findPostsByTagName(request.tagName, toPageableLatestSort(request.page, request.size)) ?: throw PostNotFoundException()
        return posts.content.map { it.toDto() }
    }

    fun getPostsByUserId(getPostsRequest: GetUserPostsDto): List<PostResponseDto> {
        val posts = customPostRepository.findPostsByUserId(
            getPostsRequest.userId,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content.map { it.toDto() }
    }

    fun findValidPostById(postId: Long): Post {
        return customPostRepository.findPostById(postId) ?: throw PostNotFoundException()
    }

    private fun createTags(tags: List<String>, post: Post): List<PostTag> {
        val createdTags = mutableListOf<PostTag>()
        tags.forEach {
            var tag = tagService.findValidTagByName(it)
            if (tag == null) {
                tag = tagService.createTag(it)
            }
            val postTag = PostTag(
                post = post,
                tag = tag
            )
            createdTags.add(postTag)
        }
        return createdTags
    }

    private fun checkPostOwner(post: Post, user: User) {
        if (post.user.id != user.id) {
            throw AccessPostWithNoAuthException()
        }
    }

    private fun updatePostArticle(post: Post, updatePostRequestDto: UpdatePostRequestDto) {
        post.title = updatePostRequestDto.title
        post.content = updatePostRequestDto.content
        post.postTags = createTags(updatePostRequestDto.tags, post) as MutableList<PostTag>
    }


    private fun CreatePostRequestDto.toEntity(spot: Spot, user: User): Post =
        Post(
            title = title,
            user = user,
            content = content,
            imageUrl = imageUrls,
            spot = spot
        )

    private fun toPageableLatestSort(page: Int, size: Int) = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id")
}
