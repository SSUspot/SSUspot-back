package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.*
import com.ssuspot.sns.application.dto.user.UserResponseDto
import com.ssuspot.sns.application.service.spot.SpotService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.exceptions.post.AccessPostWithNoAuthException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.spot.entity.Spot
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
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
    @Transactional(readOnly = true)
    @Cacheable(value = ["recommended-posts"], key = "#getRecommendPostsDto.email + '_' + #getRecommendPostsDto.page + '_' + #getRecommendPostsDto.size")
    fun getRecommendedPosts(getRecommendPostsDto: GetRecommendedPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getRecommendPostsDto.email)
        val posts = customPostRepository.findRecommendedPosts(
            user,
            toPageableLatestSort(getRecommendPostsDto.page, getRecommendPostsDto.size)
        )
        return posts.content
    }
    @Transactional
    @Caching(evict = [
        CacheEvict(value = ["posts"], allEntries = true),
        CacheEvict(value = ["post-summaries"], allEntries = true),
        CacheEvict(value = ["user-posts"], allEntries = true),
        CacheEvict(value = ["spot-posts"], allEntries = true),
        CacheEvict(value = ["recommended-posts"], allEntries = true),
        CacheEvict(value = ["following-posts"], allEntries = true)
    ])
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
    fun updatePost(updatePostRequestDto: UpdatePostRequestDto): PostResponseDto {
        val post = findValidPostById(updatePostRequestDto.postId)
        val user = userService.findValidUserByEmail(updatePostRequestDto.email)
        checkPostOwner(post, user)
        updatePostArticle(post, updatePostRequestDto)
        return customPostRepository.save(post).toDto()
    }

    fun getFollowingPosts(getPostsRequest: GetFollowingPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = customPostRepository.findPostsByFollowingUsers(
            user,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content
    }

    fun getMyPosts(getPostsRequest: GetMyPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = customPostRepository.findPostsByUserId(
            user,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content
    }

    fun deletePost(specificPostRequestDto: SpecificPostRequestDto) {
        val post = findValidPostById(specificPostRequestDto.postId)
        val user = userService.findValidUserByEmail(specificPostRequestDto.email)
        checkPostOwner(post, user)
        customPostRepository.delete(post)
    }


    fun getPostById(specificPostRequestDto: SpecificPostRequestDto): PostResponseDto {
        val user = userService.findValidUserByEmail(specificPostRequestDto.email)
        val post =
            customPostRepository.findPostById(specificPostRequestDto.postId, user) ?: throw PostNotFoundException()
        return post
    }

    fun getPostsBySpotId(getPostsBySpotIdDto: GetPostsBySpotIdDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsBySpotIdDto.email)
        val posts = customPostRepository.findPostsBySpotId(
            getPostsBySpotIdDto.spotId,
            user,
            toPageableLatestSort(getPostsBySpotIdDto.page, getPostsBySpotIdDto.size)
        )
        return posts.content
    }

    fun findPostsByTagName(request: GetTagRequestDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(request.email)
        val posts =
            customPostRepository.findPostsByTagName(
                request.tagName,
                user,
                toPageableLatestSort(request.page, request.size)
            )
                ?: throw PostNotFoundException()
        return posts.content
    }

    fun getPostsByUserId(getPostsRequest: GetUserPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = customPostRepository.findPostsByUserId(
            user,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content
    }

    fun findValidPostById(postId: Long): Post {
        return customPostRepository.findValidPostById(postId)
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



    private fun toPageableLatestSort(page: Int, size: Int) = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id")
}
