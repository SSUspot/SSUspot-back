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
import com.ssuspot.sns.infrastructure.validation.InputSanitizer
import com.ssuspot.sns.infrastructure.monitoring.BusinessMetricsCollector
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
    private val tagService: TagService,
    private val inputSanitizer: InputSanitizer,
    private val businessMetricsCollector: BusinessMetricsCollector
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
        val startTime = System.currentTimeMillis()
        
        // 입력 데이터 검증 및 정제
        val sanitizedTitle = inputSanitizer.sanitizePostTitle(createPostRequestDto.title)
        val sanitizedContent = inputSanitizer.sanitizePostContent(createPostRequestDto.content)
        val sanitizedTags = createPostRequestDto.tags.map { inputSanitizer.sanitizeTagName(it) }
        
        // 이미지 URL 검증
        createPostRequestDto.imageUrls.forEach { imageUrl ->
            if (!inputSanitizer.isValidUrl(imageUrl)) {
                throw IllegalArgumentException("잘못된 이미지 URL 형식입니다: $imageUrl")
            }
        }
        
        val sanitizedDto = createPostRequestDto.copy(
            title = sanitizedTitle,
            content = sanitizedContent,
            tags = sanitizedTags
        )
        
        val user = userService.findValidUserByEmail(sanitizedDto.userEmail)
        val spot = spotService.findValidSpot(sanitizedDto.spotId)
        
        val post = sanitizedDto.toEntity(spot, user)
        var savedPost = customPostRepository.save(post)
        val postTags = createTags(sanitizedDto.tags, savedPost)
        savedPost.postTags = postTags as MutableList<PostTag>
        savedPost = customPostRepository.save(savedPost)
        
        // 비즈니스 메트릭 수집
        val duration = System.currentTimeMillis() - startTime
        businessMetricsCollector.recordPostCreation(
            postId = savedPost.id.toString(),
            userId = user.id.toString(),
            spotId = spot.id.toString(),
            duration = duration
        )
        
        return savedPost.toDto()
    }

    @Transactional
    @Caching(evict = [
        CacheEvict(value = ["posts"], allEntries = true),
        CacheEvict(value = ["postsByUser"], allEntries = true),
        CacheEvict(value = ["postsBySpot"], allEntries = true),
        CacheEvict(value = ["postsByTag"], allEntries = true),
        CacheEvict(value = ["recommendedPosts"], allEntries = true),
        CacheEvict(value = ["followingPosts"], allEntries = true)
    ])
    fun updatePost(updatePostRequestDto: UpdatePostRequestDto): PostResponseDto {
        val post = findValidPostById(updatePostRequestDto.postId)
        val user = userService.findValidUserByEmail(updatePostRequestDto.email)
        checkPostOwner(post, user)
        updatePostArticle(post, updatePostRequestDto)
        return customPostRepository.save(post).toDto()
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["followingPosts"], key = "#getPostsRequest.email + '_' + #getPostsRequest.page + '_' + #getPostsRequest.size")
    fun getFollowingPosts(getPostsRequest: GetFollowingPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = customPostRepository.findPostsByFollowingUsers(
            user,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["postsByUser"], key = "#getPostsRequest.email + '_' + #getPostsRequest.page + '_' + #getPostsRequest.size")
    fun getMyPosts(getPostsRequest: GetMyPostsDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsRequest.email)
        val posts = customPostRepository.findPostsByUserId(
            user,
            toPageableLatestSort(getPostsRequest.page, getPostsRequest.size)
        )
        return posts.content
    }

    @Transactional
    @Caching(evict = [
        CacheEvict(value = ["posts"], allEntries = true),
        CacheEvict(value = ["postsByUser"], allEntries = true),
        CacheEvict(value = ["postsBySpot"], allEntries = true),
        CacheEvict(value = ["postsByTag"], allEntries = true),
        CacheEvict(value = ["recommendedPosts"], allEntries = true),
        CacheEvict(value = ["followingPosts"], allEntries = true)
    ])
    fun deletePost(specificPostRequestDto: SpecificPostRequestDto) {
        val post = findValidPostById(specificPostRequestDto.postId)
        val user = userService.findValidUserByEmail(specificPostRequestDto.email)
        checkPostOwner(post, user)
        customPostRepository.delete(post)
    }


    @Transactional(readOnly = true)
    @Cacheable(value = ["posts"], key = "#specificPostRequestDto.postId + '_' + #specificPostRequestDto.email")
    fun getPostById(specificPostRequestDto: SpecificPostRequestDto): PostResponseDto {
        val user = userService.findValidUserByEmail(specificPostRequestDto.email)
        val post =
            customPostRepository.findPostById(specificPostRequestDto.postId, user) ?: throw PostNotFoundException()
        
        // 비즈니스 메트릭 수집 - 게시물 조회
        businessMetricsCollector.recordPostView(
            postId = specificPostRequestDto.postId.toString(),
            userId = user.id?.toString()
        )
        
        return post
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["postsBySpot"], key = "#getPostsBySpotIdDto.spotId + '_' + #getPostsBySpotIdDto.email + '_' + #getPostsBySpotIdDto.page + '_' + #getPostsBySpotIdDto.size")
    fun getPostsBySpotId(getPostsBySpotIdDto: GetPostsBySpotIdDto): List<PostResponseDto> {
        val user = userService.findValidUserByEmail(getPostsBySpotIdDto.email)
        val posts = customPostRepository.findPostsBySpotId(
            getPostsBySpotIdDto.spotId,
            user,
            toPageableLatestSort(getPostsBySpotIdDto.page, getPostsBySpotIdDto.size)
        )
        return posts.content
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["postsByTag"], key = "#request.tagName + '_' + #request.email + '_' + #request.page + '_' + #request.size")
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
