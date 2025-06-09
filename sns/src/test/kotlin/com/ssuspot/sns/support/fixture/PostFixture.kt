package com.ssuspot.sns.support.fixture

import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.application.dto.post.CreatePostRequestDto
import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.application.dto.user.UserResponseDto

object PostFixture {
    
    fun createPost(
        id: Long? = 1L,
        user: User = UserFixture.createUser(),
        spot: Spot = SpotFixture.createSpot(),
        title: String = "Test Post Title",
        content: String = "This is a test post content",
        viewCount: Long = 0,
        likeCount: Long = 0,
        rating: Double? = 0.0,
        imageUrls: List<String> = listOf("https://example.com/image1.jpg")
    ): Post {
        return Post(
            id = id,
            user = user,
            spot = spot,
            title = title,
            content = content,
            viewCount = viewCount,
            likeCount = likeCount,
            rating = rating,
            imageUrls = imageUrls
        )
    }
    
    fun createCreatePostRequestDto(
        title: String = "Test Post Title",
        content: String = "This is a test post content",
        userEmail: String = "test@example.com",
        imageUrls: List<String> = listOf("https://example.com/image1.jpg"),
        tags: List<String> = listOf("tag1", "tag2"),
        spotId: Long = 1L
    ): CreatePostRequestDto {
        return CreatePostRequestDto(
            title = title,
            content = content,
            userEmail = userEmail,
            imageUrls = imageUrls,
            tags = tags,
            spotId = spotId
        )
    }
    
    fun createPostResponseDto(
        id: Long = 1L,
        title: String = "Test Post Title",
        content: String = "This is a test post content",
        user: UserResponseDto = UserFixture.createUserResponseDto(),
        imageUrls: List<String> = listOf("https://example.com/image1.jpg"),
        tags: List<String> = listOf("tag1", "tag2"),
        spotId: Long = 1L,
        createdAt: String = "2024-01-01T00:00:00",
        updatedAt: String = "2024-01-01T00:00:00",
        hasLiked: Boolean = false
    ): PostResponseDto {
        return PostResponseDto(
            id = id,
            title = title,
            content = content,
            user = user,
            imageUrls = imageUrls,
            tags = tags,
            spotId = spotId,
            createdAt = createdAt,
            updatedAt = updatedAt,
            hasLiked = hasLiked
        )
    }
    
    fun createPosts(count: Int, user: User = UserFixture.createUser(), spot: Spot = SpotFixture.createSpot()): List<Post> {
        return (1..count).map { i ->
            createPost(
                id = i.toLong(),
                user = user,
                spot = spot,
                title = "Test Post Title $i",
                content = "This is test post content $i"
            )
        }
    }
}