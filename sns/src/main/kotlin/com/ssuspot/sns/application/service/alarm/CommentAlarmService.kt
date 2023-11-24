package com.ssuspot.sns.application.service.alarm

import com.ssuspot.sns.application.dto.alarm.CommentAlarmDto
import com.ssuspot.sns.application.dto.alarm.CommentAlarmResponseDto
import com.ssuspot.sns.application.service.post.CommentService
import com.ssuspot.sns.application.service.post.PostService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.alarm.CommentAlarm
import com.ssuspot.sns.domain.model.post.repository.CustomCommentAlarmRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentAlarmService(
    private val customCommentAlarmRepository: CustomCommentAlarmRepository,
    private val userService: UserService,
    private val postService: PostService,
    private val commentService: CommentService,
) {
    @Transactional
    fun saveCommentAlarm(requestDto: CommentAlarmDto) {
        val articleUser = userService.getValidUser(requestDto.postUserId)
        val commentUser = userService.getValidUser(requestDto.commentUserId)
        val post = postService.findValidPostById(requestDto.postId)
        val comment = commentService.findValidCommentByCommentId(requestDto.commentId)
        customCommentAlarmRepository.save(
            CommentAlarm(
                postUser = articleUser,
                commentUser = commentUser,
                post = post,
                comment = comment
            )
        )
    }

    @Transactional
    fun getAlarms(page: Int, size: Int, email: String): List<CommentAlarmResponseDto> {
        val pageable: PageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "alarmId")
        val user = userService.getValidUserByEmail(email)
        val alarms = customCommentAlarmRepository.findCommentAlarmByUser(user, pageable)
        return alarms.content.map {
            CommentAlarmResponseDto(
                commentAlarmId = it.commentAlarmId!!,
                commentedUser = it.commentUser.nickname,
                articleTitle = it.post.title,
                commentContent = it.comment.content,
            )
        }
    }
}