package com.ssuspot.sns.infrastructure.repository.alarm

import com.ssuspot.sns.domain.model.alarm.CommentAlarm
import org.springframework.data.jpa.repository.JpaRepository

interface CommentAlarmRepository: JpaRepository<CommentAlarm, Long>{
}