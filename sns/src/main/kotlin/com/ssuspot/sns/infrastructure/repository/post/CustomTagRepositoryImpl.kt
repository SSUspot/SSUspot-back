package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.*
import com.ssuspot.sns.domain.model.post.repository.CustomTagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomTagRepository {
    override fun findPostsByTagName(tagName: String, page: Pageable): Page<Post> {
        val post = QPost.post
        val postTag = QPostTag.postTag
        val tag = QTag.tag

        val result = queryFactory
            // we need to get posts that have the at
            .select(post)
            // join post_tag on post.id = post_tag.post_id
            .from(post)
            .innerJoin(postTag).on(post.id.eq(postTag.post.id))
            // join tag on post_tag.tag_id = tag.id
            .innerJoin(tag).on(postTag.tag.id.eq(tag.id))
            .innerJoin(post.user).fetchJoin()
            // where tag.tag_name = tagName
            .where(tag.tagName.eq(tagName))
            // order by post.id desc
            .orderBy(post.id.desc())
            // offset page.offset
            .offset(page.offset)
            // limit page.pageSize
            .limit(page.pageSize.toLong())
            // fetch results
            .fetchResults()

        val content = result.results
        val total = result.total

        return PageImpl(content, page, total)
    }


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

    override fun findPostsByTagNameIn(names: List<String>, page: Pageable): Page<Post>? {
        val post = QPost.post
        val postTag = QPostTag.postTag

        val result = queryFactory
            .select(post)
            .from(post)
            .innerJoin(postTag).on(post.id.eq(postTag.post.id))
            .where(postTag.tag.tagName.`in`(names))
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetchResults()

        val content = result.results
        val total = result.total

        return PageImpl(content, page, total)
    }
}
