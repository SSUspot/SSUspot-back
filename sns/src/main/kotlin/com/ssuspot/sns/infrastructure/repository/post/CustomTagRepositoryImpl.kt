package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.*
import com.ssuspot.sns.domain.model.post.repository.CustomTagRepository
import org.springframework.stereotype.Repository

@Repository
class CustomTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomTagRepository {
    override fun findTagByTagName(name: String): Tag? {
        return queryFactory.selectFrom(QTag.tag)
            .where(QTag.tag.tagName.eq(name))
            .fetchOne()
    }

    override fun findTagsByTagNameIn(names: List<String>): List<Tag>? {
        return queryFactory.selectFrom(QTag.tag)
            .where(QTag.tag.tagName.`in`(names))
            .fetch()
    }
}
