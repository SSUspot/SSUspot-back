/*
package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.entity.QComment
import com.ssuspot.sns.domain.model.post.repository.CustomCommentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomCommentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomCommentRepository {
    override fun findCommentById(commentId: Long): Comment? {
        return queryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.id.eq(commentId))
            .fetchOne()
    }

    override fun findCommentsByPostId(postId: Long, pageable: Pageable): Page<Comment> {
        val query = queryFactory.selectFrom(QComment.comment)
            .where(QComment.comment.post.id.eq(postId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.selectFrom(QComment.comment)
            .where(QComment.comment.post.id.eq(postId))
            .fetchCount()

        return PageImpl(
            results,
            pageable,
            total
        )
    }

    override fun findCommentsByUserId(userId: Long, pageable: Pageable): Page<Comment> {
        val query = queryFactory.selectFrom(QComment.comment)
            .where(QComment.comment.user.id.eq(userId))

        val results = query.fetch()
        val total = queryFactory.selectFrom(QComment.comment)
            .where(QComment.comment.user.id.eq(userId))
            .fetchCount()

        return PageImpl(
            results,
            pageable,
            total
        )
    }
}
*/

package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.Comment
import com.ssuspot.sns.domain.model.post.repository.CustomCommentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomCommentRepositoryImpl : CustomCommentRepository {
    override fun findCommentById(commentId: Long): Comment? {
        throw UnsupportedOperationException("QueryDSL not configured yet")
    }

    override fun findCommentsByPostId(postId: Long, pageable: Pageable): Page<Comment> {
        throw UnsupportedOperationException("QueryDSL not configured yet")
    }

    override fun findCommentsByUserId(userId: Long, pageable: Pageable): Page<Comment> {
        throw UnsupportedOperationException("QueryDSL not configured yet")
    }
}