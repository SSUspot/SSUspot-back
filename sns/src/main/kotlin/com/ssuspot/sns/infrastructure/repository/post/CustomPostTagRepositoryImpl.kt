package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.entity.QPostTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostTagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository


@Repository
class CustomPostTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomPostTagRepository {
    override fun findPostTagsByPostId(
        postId: Long,
        pageable: Pageable
    ): Page<PostTag>? {
        val query = queryFactory.selectFrom(QPostTag.postTag)
            .where(QPostTag.postTag.post.id.eq(postId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val result = query.fetch()
        val total = queryFactory.selectFrom(QPostTag.postTag)
            .where(QPostTag.postTag.post.id.eq(postId))
            .fetchCount()
        return PageImpl(
            result,
            pageable,
            total
        )
    }

    override fun findPostTagsByTagName(
        tagName: String
    ): List<PostTag>? {
        return queryFactory.selectFrom(QPostTag.postTag)
            .where(QPostTag.postTag.tag.tagName.eq(tagName))
            .fetch()
    }
}