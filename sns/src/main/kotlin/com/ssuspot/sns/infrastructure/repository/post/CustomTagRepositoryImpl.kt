/*
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
*/

package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.domain.model.post.repository.CustomTagRepository
import org.springframework.stereotype.Repository

@Repository
class CustomTagRepositoryImpl : CustomTagRepository {
    override fun findTagByTagName(name: String): Tag? {
        throw UnsupportedOperationException("QueryDSL not configured yet")
    }

    override fun findTagsByTagNameIn(names: List<String>): List<Tag>? {
        throw UnsupportedOperationException("QueryDSL not configured yet")
    }
}
