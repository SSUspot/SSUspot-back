package com.ssuspot.sns.infrastructure.repository.post

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.model.post.entity.*
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import com.ssuspot.sns.domain.model.user.entity.QUser
import com.ssuspot.sns.domain.model.user.entity.QUserFollow
import com.ssuspot.sns.domain.model.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

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
    override fun findValidPostById(postId: Long): Post {
        return postRepository.findById(postId).orElseThrow { throw PostNotFoundException() }
    }

    @Transactional(readOnly = true)
    override fun findPostById(postId: Long, user: User): PostResponseDto? {
        val post = queryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.id.eq(postId))
            .fetchOne()

        return post?.let {
            val hasLiked = it.postLikes.any { postLike -> postLike.user.id == user.id }
            it.toDto().apply { this.hasLiked = hasLiked }
        }
    }


    @Transactional(readOnly = true)
    override fun findPostsBySpotId(spotId: Long, user: User, pageable: Pageable): Page<PostResponseDto> {
        val query = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.spot.id.eq(spotId))
            .orderBy(QPost.post.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.spot.id.eq(spotId))
            .fetchCount()

        val postResponses = results.map { post ->
            val hasLiked = post.postLikes.any { it.user.id == user.id }
            post.toDto().apply { this.hasLiked = hasLiked }
        }

        return PageImpl(
            postResponses,
            pageable,
            total
        )
    }

    @Transactional(readOnly = true)
    override fun findPostsByUserId(user: User, pageable: Pageable): Page<PostResponseDto> {
        val query = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.user.id.eq(user.id))
            .orderBy(QPost.post.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = queryFactory.selectFrom(QPost.post)
            .where(QPost.post.user.id.eq(user.id))
            .fetchCount()

        val postResponses = results.map { post ->
            val hasLiked = post.postLikes.any { it.user.id == user.id }
            post.toDto().apply { this.hasLiked = hasLiked }
        }

        return PageImpl(
            postResponses,
            pageable,
            total
        )
    }


    @Transactional(readOnly = true)
    override fun findPostsByTagName(tagName: String, user: User, pageable: Pageable): Page<PostResponseDto> {
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

        val postResponses = posts.map { post ->
            val hasLiked = post.postLikes.any { it.user.id == user.id }
            post.toDto().apply { this.hasLiked = hasLiked }
        }

        return PageImpl(postResponses, pageable, total)
    }

    @Transactional(readOnly = true)
    override fun findPostsByFollowingUsers(user: User, pageable: Pageable): Page<PostResponseDto> {
        val post = QPost.post
        val userFollow = QUserFollow.userFollow
        val followingUser = QUser.user
        val postLike = QPostLike.postLike

        val followingUsers = queryFactory.selectFrom(userFollow)
            .where(userFollow.followingUser.eq(user))
            .select(userFollow.followedUser)
            .fetch()

        val posts = queryFactory.selectFrom(post)
            .join(post.user, followingUser)
            .where(followingUser.`in`(followingUsers))
            .orderBy(post.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        val postResponses = posts.map { post ->
            val hasLiked = queryFactory.selectFrom(postLike)
                .where(postLike.post.eq(post), postLike.user.eq(user))
                .fetchCount() > 0
            post.toDto().apply { this.hasLiked = hasLiked }
        }

        val total = queryFactory.selectFrom(post)
            .where(post.user.`in`(followingUsers))
            .fetchCount()

        return PageImpl(postResponses, pageable, total)
    }



}