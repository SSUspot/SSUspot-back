package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.CommentResponseDto
import com.ssuspot.sns.application.dto.post.CreateCommentDto
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.repository.post.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val postService: PostService
) {
    @Transactional
    fun createComment(createCommentDto: CreateCommentDto): CommentResponseDto {
        val comment = createCommentDto.toEntity(
            post = postService.findValidPostById(createCommentDto.postId),
            user = userService.findValidUserByEmail(createCommentDto.userEmail)
        )
        val savedComment = commentRepository.save(comment)
        return savedComment.toDto()
    }

    private fun CreateCommentDto.toEntity(post: Post, user: User): Comment =
        Comment(
            post = post,
            user = user,
            content = content,
        )

    private fun Comment.toDto(): CommentResponseDto =
        CommentResponseDto(
            id = id!!,
            postId = post.id!!,
            userEmail = user.email,
            content = content,
        )
}