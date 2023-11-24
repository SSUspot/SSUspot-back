package com.ssuspot.sns.domain.model.post.repository

import com.ssuspot.sns.domain.model.alarm.CommentAlarm
import com.ssuspot.sns.domain.model.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomCommentAlarmRepository {
    fun save(commentAlarm: CommentAlarm): CommentAlarm
    fun findCommentAlarmByUser(user: User, pageable: Pageable): Page<CommentAlarm>
}