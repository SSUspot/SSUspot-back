package com.ssuspot.sns.application.service.alarm

import com.ssuspot.sns.application.dto.alarm.AlarmDto
import com.ssuspot.sns.application.dto.alarm.AlarmResponseDto
import com.ssuspot.sns.application.service.post.CommentService
import com.ssuspot.sns.application.service.post.PostService
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.alarm.Alarm
import com.ssuspot.sns.infrastructure.repository.alarm.AlarmRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlarmService(
    private val alarmRepository: AlarmRepository,
    private val userService: UserService,
    private val postService: PostService,
    private val commentService: CommentService,
) {
    @Transactional
    fun saveAlarm(requestDto: AlarmDto) {
        val articleUser = userService.getValidUser(requestDto.postUserId)
        val commentUser = userService.getValidUser(requestDto.commentUserId)
        val post = postService.findValidPostById(requestDto.postId)
        val comment = commentService.findValidCommentByCommentId(requestDto.commentId)
        alarmRepository.save(
            Alarm(
                articleUser = articleUser,
                commentUser = commentUser,
                post = post,
                comment = comment
            )
        )
    }

    @Transactional
    fun getAlarms(page: Int, size: Int, userId: String): List<AlarmResponseDto> {
        val pageable: PageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "alarmId")
        val user = userService.getValidUserByEmail(userId)
        val alarms = alarmRepository.findAlarmByArticleUserUserId(user.id!!, pageable)
        return alarms.content.map {
            val post = postService.findValidPostById(it.post.id!!)
            val comment = commentService.findValidCommentByCommentId(it.comment.id!!)
            AlarmResponseDto(
                it.alarmId!!,
                post.title,
                comment.content,
            )
        }
    }
}