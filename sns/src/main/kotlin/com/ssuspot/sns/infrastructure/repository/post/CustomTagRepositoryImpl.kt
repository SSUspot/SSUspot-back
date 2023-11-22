package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.exceptions.post.TagNotFoundException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.QTag
import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.domain.model.post.repository.CustomTagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomTagRepository {
    override fun findPostsByTagName(tagName: String, pageable: Pageable): Page<Post>? {
        //should join PostTag and join Post
        val query = queryFactory.selectFrom(QTag.tag)
            .join(QTag.tag.postTags)
            .join(QTag.tag.postTags.any().post)
            .where(QTag.tag.tagName.eq(tagName))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val result = query.fetch()
        val total = queryFactory.selectFrom(QTag.tag)
            .join(QTag.tag.postTags)
            .join(QTag.tag.postTags.any().post)
            .where(QTag.tag.tagName.eq(tagName))
            .fetchCount()
        return PageImpl(
            result.map { it.postTags.map { it.post } }.flatten(),
            pageable,
            total
        )
    }

    override fun findTagByTagName(name: String): Tag? {
        return queryFactory.selectFrom(QTag.tag)
            .where(QTag.tag.tagName.eq(name))
            .fetchOne()
    }

    override fun findTagsByTagNames(names: List<String>): List<Tag>? {
        return queryFactory.selectFrom(QTag.tag)
            .where(QTag.tag.tagName.`in`(names))
            .fetch()
    }

    override fun findPostsByTagNames(names: List<String>, pageable: Pageable): Page<Post>? {
        //should join PostTag and join Post
        val query = queryFactory.selectFrom(QTag.tag)
            .join(QTag.tag.postTags)
            .join(QTag.tag.postTags.any().post)
            .where(QTag.tag.tagName.`in`(names))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        val result = query.fetch()
        val total = queryFactory.selectFrom(QTag.tag)
            .join(QTag.tag.postTags)
            .join(QTag.tag.postTags.any().post)
            .where(QTag.tag.tagName.`in`(names))
            .fetchCount()
        return PageImpl(
            result.map { it.postTags.map { it.post } }.flatten(),
            pageable,
            total
        )
    }
}
