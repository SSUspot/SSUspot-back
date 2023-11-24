package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.QPost
import com.ssuspot.sns.domain.model.post.entity.QPostTag
import com.ssuspot.sns.domain.model.post.entity.QTag
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomPostRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val postRepository: PostRepository,
) : CustomPostRepository {
    override fun save(post: Post): Post {
        return postRepository.save(post)
    }
    override fun delete(post: Post) {
        postRepository.delete(post)
    }

    override fun findPostById(postId: Long): Post? {
        return queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.id.eq(postId))
            .fetchOne()
    }

    override fun findPostsBySpotId(spotId: Long, pageable: Pageable): Page<Post> {
        val query = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.spot.id.eq(spotId))
            .orderBy(QPost.post.createdAt.desc())
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
            .orderBy(QPost.post.createdAt.desc())
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

    override fun findPostsByTagName(tagName: String, pageable: Pageable): Page<Post> {
        val post = QPost.post
        val postTag = QPostTag.postTag
        val tag = QTag.tag

        val posts = queryFactory.selectFrom(post)
            .join(post.postTags, postTag)
            .join(postTag.tag, tag)
            .where(tag.tagName.eq(tagName))
            .orderBy(post.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory.selectFrom(post)
            .join(post.postTags, postTag)
            .join(postTag.tag, tag)
            .where(tag.tagName.eq(tagName))
            .fetchCount()

        return PageImpl(posts, pageable, total)
    }

    override fun findPostsByTagNameIn(tagNames: List<String>, page: Pageable): Page<Post>? {
        val post = QPost.post
        val postTag = QPostTag.postTag
        val result = queryFactory
            .select(post)
            .from(post)
            .innerJoin(postTag).on(post.id.eq(postTag.post.id))
            .where(postTag.tag.tagName.`in`(tagNames))
            .orderBy(post.createdAt.desc())
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetchResults()

        val content = result.results
        val total = result.total

        return PageImpl(content, page, total)
    }
}