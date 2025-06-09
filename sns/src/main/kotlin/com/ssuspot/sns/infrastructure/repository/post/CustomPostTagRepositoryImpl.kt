/*
package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.entity.QPostTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostTagRepository
import org.springframework.stereotype.Repository


@Repository
class CustomPostTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomPostTagRepository {
    override fun findPostTagsByTagTagName(
        tagName: String
    ): List<PostTag>? {
        return queryFactory.selectFrom(QPostTag.postTag)
            .where(QPostTag.postTag.tag.tagName.eq(tagName))
            .fetch()
    }
}
*/

package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostTagRepository
import org.springframework.stereotype.Repository

@Repository
class CustomPostTagRepositoryImpl : CustomPostTagRepository {
    override fun findPostTagsByTagTagName(tagName: String): List<PostTag>? {
        throw UnsupportedOperationException("QueryDSL not configured yet")
    }
}