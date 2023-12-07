package com.ssuspot.sns.application.service.post

import com.ssuspot.sns.application.dto.post.CommentResponseDto
import com.ssuspot.sns.application.dto.post.CreateCommentDto
import com.ssuspot.sns.application.dto.post.GetCommentDto
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.exceptions.post.AccessCommentWithoutNoAuthException
import com.ssuspot.sns.domain.exceptions.post.CommentNotFoundException
import com.ssuspot.sns.domain.model.alarm.event.CommentAlarmEvent
import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.repository.post.CommentRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val postService: PostService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun createComment(createCommentDto: CreateCommentDto): CommentResponseDto {
        val comment = createCommentDto.toEntity(
            post = postService.findValidPostById(createCommentDto.postId),
            user = userService.findValidUserByEmail(createCommentDto.userEmail)
        )
        val savedComment = commentRepository.save(comment)
        if (savedComment.post.user.id != savedComment.user.id) {
            val commentAlarmEvent = CommentAlarmEvent(
                postUserId = savedComment.post.user.id!!,
                postId = savedComment.post.id!!,
                commentUserId = savedComment.user.id!!,
                commentId = savedComment.id!!
            )
            applicationEventPublisher.publishEvent(commentAlarmEvent)
        }
        return savedComment.toDto()
    }

    @Transactional(readOnly = true)
    fun getCommentsByPostId(getCommentDto: GetCommentDto): List<CommentResponseDto> {
        val comments = commentRepository.findCommentsByPostId(
            getCommentDto.postId,
            toPageableLatestSort(getCommentDto.page, getCommentDto.size)
        )
        return comments.content.map { it.toDto() }
    }

    // get specific comment by comment id
    @Transactional(readOnly = true)
    fun getCommentById(
        commentId: Long
    ): CommentResponseDto {
        val comment = commentRepository.findCommentById(commentId) ?: throw CommentNotFoundException()
        return comment.toDto()
    }

    @Transactional
    fun updateComment(commentId: Long, content: String, email: String): CommentResponseDto {
        val comment = commentRepository.findCommentById(commentId) ?: throw CommentNotFoundException()
        val user = userService.findValidUserByEmail(email)
        checkCommentOwner(comment, user)
        comment.content = content
        return comment.toDto()
    }

    @Transactional
    fun deleteComment(commentId: Long, email: String) {
        val comment = commentRepository.findCommentById(commentId) ?: throw CommentNotFoundException()
        val user = userService.findValidUserByEmail(email)
        checkCommentOwner(comment, user)
        commentRepository.delete(comment)
    }


    private fun checkCommentOwner(comment: Comment, user: User) {
        if (comment.user.id != user.id) {
            throw AccessCommentWithoutNoAuthException()
        }
    }

    fun findValidCommentByCommentId(commentId: Long): Comment {
        return commentRepository.findCommentById(commentId) ?: throw CommentNotFoundException()
    }

    private fun toPageableLatestSort(page: Int, size: Int) = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id")
}