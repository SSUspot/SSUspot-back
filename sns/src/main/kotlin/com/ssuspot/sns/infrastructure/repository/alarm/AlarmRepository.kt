package com.ssuspot.sns.infrastructure.repository.alarm

import com.ssuspot.sns.domain.model.alarm.CommentAlarm
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmRepository: JpaRepository<CommentAlarm, Long>{
    //find alarm By PostUser UserId
    fun findByPostUserId(id: Long, pageable: Pageable): Page<CommentAlarm>
}