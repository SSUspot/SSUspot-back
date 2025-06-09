package com.ssuspot.sns.support.fixture

import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.application.dto.post.CommentResponseDto
import com.ssuspot.sns.application.dto.post.CreateCommentDto
import com.ssuspot.sns.application.dto.user.UserResponseDto

object CommentFixture {
    
    fun createComment(
        id: Long? = 1L,
        post: Post = PostFixture.createPost(),
        user: User = UserFixture.createUser(),
        content: String = "This is a test comment"
    ): Comment {
        return Comment(
            id = id,
            post = post,
            user = user,
            content = content
        )
    }
    
    fun createCreateCommentDto(
        postId: Long = 1L,
        userEmail: String = "test@example.com",
        content: String = "This is a test comment"
    ): CreateCommentDto {
        return CreateCommentDto(
            postId = postId,
            userEmail = userEmail,
            content = content
        )
    }
    
    fun createCommentResponseDto(
        id: Long = 1L,
        postId: Long = 1L,
        user: UserResponseDto = UserFixture.createUserResponseDto(),
        content: String = "This is a test comment"
    ): CommentResponseDto {
        return CommentResponseDto(
            id = id,
            postId = postId,
            user = user,
            content = content
        )
    }
    
    fun createComments(count: Int, post: Post = PostFixture.createPost(), user: User = UserFixture.createUser()): List<Comment> {
        return (1..count).map { i ->
            createComment(
                id = i.toLong(),
                post = post,
                user = user,
                content = "This is test comment $i"
            )
        }
    }
}