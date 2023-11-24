package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.alarm.CommentAlarm
import com.ssuspot.sns.domain.model.alarm.QCommentAlarm
import com.ssuspot.sns.domain.model.post.repository.CustomCommentAlarmRepository
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.infrastructure.repository.alarm.CommentAlarmRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomCommentAlarmRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val commentAlarmRepository: CommentAlarmRepository
): CustomCommentAlarmRepository {
    override fun save(commentAlarm: CommentAlarm): CommentAlarm {
        return commentAlarmRepository.save(commentAlarm)
    }
    override fun findCommentAlarmByUser(user: User, pageable: Pageable): Page<CommentAlarm> {
        val commentAlarm = QCommentAlarm.commentAlarm
        val query = queryFactory.selectFrom(commentAlarm)
            .where(commentAlarm.postUser.eq(user))
            .orderBy(commentAlarm.comment.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.selectFrom(commentAlarm)
            .where(commentAlarm.postUser.eq(user))
            .fetchCount()
        return PageImpl(
            results,
            pageable,
            total
        )
    }
}