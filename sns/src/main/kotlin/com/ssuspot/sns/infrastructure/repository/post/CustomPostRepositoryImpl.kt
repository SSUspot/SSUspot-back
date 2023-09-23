package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.QPost
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomPostRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomPostRepository {
    override fun findPostById(postId: Long): Post? {
        return queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.id.eq(postId))
            .fetchOne()
    }

    override fun findPostsBySpotId(spotId: Long, pageable: Pageable): Page<Post> {
        val query = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.spot.id.eq(spotId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.spot.id.eq(spotId))
            .fetchCount()

        return PageImpl(
            results,
            pageable,
            total
        )
    }

    override fun findPostsByUserId(userId: Long, pageable: Pageable): Page<Post> {
        val query = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.user.id.eq(userId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.user.id.eq(userId))
            .fetchCount()

        return PageImpl(
            results,
            pageable,
            total
        )
    }
}