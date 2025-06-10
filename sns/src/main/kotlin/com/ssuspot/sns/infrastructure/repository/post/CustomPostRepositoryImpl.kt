
package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.application.dto.post.PostResponseDto
import com.ssuspot.sns.domain.exceptions.post.PostNotFoundException
import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.repository.CustomPostRepository
import com.ssuspot.sns.domain.model.user.entity.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CustomPostRepositoryImpl(
    private val postRepository: PostRepository,
    @PersistenceContext private val entityManager: EntityManager
) : CustomPostRepository {
    
    override fun save(post: Post): Post {
        return postRepository.save(post)
    }

    override fun delete(post: Post) {
        postRepository.delete(post)
    }

    override fun findValidPostById(postId: Long): Post {
        return postRepository.findById(postId).orElseThrow { PostNotFoundException() }
    }

    @Transactional(readOnly = true)
    override fun findPostById(postId: Long, user: User): PostResponseDto? {
        // 먼저 Post와 연관 엔티티들을 JOIN FETCH로 효율적으로 로드
        val query = """
            SELECT DISTINCT p FROM Post p 
            LEFT JOIN FETCH p.user 
            LEFT JOIN FETCH p.spot 
            LEFT JOIN FETCH p.postTags pt 
            LEFT JOIN FETCH pt.tag
            WHERE p.id = :postId
        """.trimIndent()
        
        val posts = entityManager.createQuery(query, Post::class.java)
            .setParameter("postId", postId)
            .resultList
            
        val post = posts.firstOrNull() ?: return null
        
        // 별도 쿼리로 좋아요 여부 확인 (메모리 효율성)
        val hasLikedQuery = """
            SELECT COUNT(pl) > 0 FROM PostLike pl 
            WHERE pl.post.id = :postId AND pl.user.id = :userId
        """.trimIndent()
        
        val hasLiked = entityManager.createQuery(hasLikedQuery, Boolean::class.java)
            .setParameter("postId", postId)
            .setParameter("userId", user.id)
            .singleResult
            
        return post.toDto().apply { this.hasLiked = hasLiked }
    }

    @Transactional(readOnly = true)
    override fun findPostsBySpotId(spotId: Long, user: User, pageable: Pageable): Page<PostResponseDto> {
        // 먼저 ID만 조회하여 페이징 처리
        val idQuery = """
            SELECT p.id FROM Post p 
            WHERE p.spot.id = :spotId 
            ORDER BY p.createdAt DESC
        """.trimIndent()
        
        val postIds = entityManager.createQuery(idQuery, Long::class.java)
            .setParameter("spotId", spotId)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            
        if (postIds.isEmpty()) {
            return PageImpl(emptyList(), pageable, 0)
        }
            
        // ID 기반으로 JOIN FETCH로 데이터 조회
        val query = """
            SELECT DISTINCT p FROM Post p 
            LEFT JOIN FETCH p.user 
            LEFT JOIN FETCH p.spot 
            LEFT JOIN FETCH p.postTags pt 
            LEFT JOIN FETCH pt.tag
            WHERE p.id IN :postIds 
            ORDER BY p.createdAt DESC
        """.trimIndent()
        
        val posts = entityManager.createQuery(query, Post::class.java)
            .setParameter("postIds", postIds)
            .resultList
            
        val postResponses = posts.map { post ->
            // 별도 쿼리로 좋아요 여부 확인
            val hasLikedQuery = """
                SELECT COUNT(pl) > 0 FROM PostLike pl 
                WHERE pl.post.id = :postId AND pl.user.id = :userId
            """.trimIndent()
            
            val hasLiked = entityManager.createQuery(hasLikedQuery, Boolean::class.java)
                .setParameter("postId", post.id)
                .setParameter("userId", user.id)
                .singleResult
                
            post.toDto().apply { this.hasLiked = hasLiked }
        }
        
        // COUNT 쿼리
        val countQuery = "SELECT COUNT(p) FROM Post p WHERE p.spot.id = :spotId"
        val total = entityManager.createQuery(countQuery, Long::class.java)
            .setParameter("spotId", spotId)
            .singleResult
        
        return PageImpl(postResponses, pageable, total)
    }

    @Transactional(readOnly = true)
    override fun findPostsByUserId(user: User, pageable: Pageable): Page<PostResponseDto> {
        val countQuery = """
            SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId
        """.trimIndent()
        
        val total = entityManager.createQuery(countQuery, Long::class.java)
            .setParameter("userId", user.id)
            .singleResult
            
        val query = """
            SELECT DISTINCT p FROM Post p 
            LEFT JOIN FETCH p.user 
            LEFT JOIN FETCH p.spot 
            LEFT JOIN FETCH p.postTags pt 
            LEFT JOIN FETCH pt.tag
            WHERE p.user.id = :userId 
            ORDER BY p.createdAt DESC
        """.trimIndent()
        
        val posts = entityManager.createQuery(query, Post::class.java)
            .setParameter("userId", user.id)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            
        val postResponses = posts.map { post ->
            // 별도 쿼리로 좋아요 여부 확인
            val hasLikedQuery = """
                SELECT COUNT(pl) > 0 FROM PostLike pl 
                WHERE pl.post.id = :postId AND pl.user.id = :userId
            """.trimIndent()
            
            val hasLiked = entityManager.createQuery(hasLikedQuery, Boolean::class.java)
                .setParameter("postId", post.id)
                .setParameter("userId", user.id)
                .singleResult
                
            post.toDto().apply { this.hasLiked = hasLiked }
        }
        
        return PageImpl(postResponses, pageable, total)
    }

    @Transactional(readOnly = true)
    override fun findPostsByTagName(tagName: String, user: User, pageable: Pageable): Page<PostResponseDto> {
        val countQuery = """
            SELECT COUNT(DISTINCT p) FROM Post p 
            INNER JOIN p.postTags pt 
            INNER JOIN pt.tag t 
            WHERE t.tagName = :tagName
        """.trimIndent()
        
        val total = entityManager.createQuery(countQuery, Long::class.java)
            .setParameter("tagName", tagName)
            .singleResult
            
        val query = """
            SELECT DISTINCT p FROM Post p 
            LEFT JOIN FETCH p.user 
            LEFT JOIN FETCH p.spot 
            LEFT JOIN FETCH p.postTags pt 
            LEFT JOIN FETCH pt.tag
            INNER JOIN p.postTags pt2 
            INNER JOIN pt2.tag t 
            WHERE t.tagName = :tagName 
            ORDER BY p.createdAt DESC
        """.trimIndent()
        
        val posts = entityManager.createQuery(query, Post::class.java)
            .setParameter("tagName", tagName)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            
        val postResponses = posts.map { post ->
            // 별도 쿼리로 좋아요 여부 확인
            val hasLikedQuery = """
                SELECT COUNT(pl) > 0 FROM PostLike pl 
                WHERE pl.post.id = :postId AND pl.user.id = :userId
            """.trimIndent()
            
            val hasLiked = entityManager.createQuery(hasLikedQuery, Boolean::class.java)
                .setParameter("postId", post.id)
                .setParameter("userId", user.id)
                .singleResult
                
            post.toDto().apply { this.hasLiked = hasLiked }
        }
        
        return PageImpl(postResponses, pageable, total)
    }

    @Transactional(readOnly = true)
    override fun findPostsByFollowingUsers(user: User, pageable: Pageable): Page<PostResponseDto> {
        val countQuery = """
            SELECT COUNT(DISTINCT p) FROM Post p 
            INNER JOIN UserFollow uf ON p.user.id = uf.followedUser.id 
            WHERE uf.followingUser.id = :userId
        """.trimIndent()
        
        val total = entityManager.createQuery(countQuery, Long::class.java)
            .setParameter("userId", user.id)
            .singleResult
            
        val query = """
            SELECT DISTINCT p FROM Post p 
            LEFT JOIN FETCH p.user 
            LEFT JOIN FETCH p.spot 
            LEFT JOIN FETCH p.postTags pt 
            LEFT JOIN FETCH pt.tag
            INNER JOIN UserFollow uf ON p.user.id = uf.followedUser.id 
            WHERE uf.followingUser.id = :userId 
            ORDER BY p.createdAt DESC
        """.trimIndent()
        
        val posts = entityManager.createQuery(query, Post::class.java)
            .setParameter("userId", user.id)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            
        val postResponses = posts.map { post ->
            // 별도 쿼리로 좋아요 여부 확인
            val hasLikedQuery = """
                SELECT COUNT(pl) > 0 FROM PostLike pl 
                WHERE pl.post.id = :postId AND pl.user.id = :userId
            """.trimIndent()
            
            val hasLiked = entityManager.createQuery(hasLikedQuery, Boolean::class.java)
                .setParameter("postId", post.id)
                .setParameter("userId", user.id)
                .singleResult
                
            post.toDto().apply { this.hasLiked = hasLiked }
        }
        
        return PageImpl(postResponses, pageable, total)
    }

    @Transactional(readOnly = true)
    override fun findRecommendedPosts(user: User, pageable: Pageable): Page<PostResponseDto> {
        // 팔로우하는 사용자들의 좋아요가 많은 게시물을 가져옴
        val popularPostQuery = """
            SELECT p.id, COUNT(pl.id) as likeCount FROM Post p 
            LEFT JOIN p.postLikes pl 
            INNER JOIN UserFollow uf ON pl.user.id = uf.followedUser.id 
            WHERE uf.followingUser.id = :userId 
            GROUP BY p.id 
            ORDER BY likeCount DESC
        """.trimIndent()
        
        val popularPostIds = entityManager.createQuery(popularPostQuery)
            .setParameter("userId", user.id)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            .map { (it as Array<*>)[0] as Long }
            
        val posts = if (popularPostIds.isEmpty()) {
            // 추천할 게시물이 없으면 최신 게시물 반환
            val latestQuery = """
                SELECT DISTINCT p FROM Post p 
                LEFT JOIN FETCH p.user 
                LEFT JOIN FETCH p.spot 
                LEFT JOIN FETCH p.postTags pt 
                LEFT JOIN FETCH pt.tag
                ORDER BY p.createdAt DESC
            """.trimIndent()
            
            entityManager.createQuery(latestQuery, Post::class.java)
                .setFirstResult(pageable.offset.toInt())
                .setMaxResults(pageable.pageSize)
                .resultList
        } else {
            val query = """
                SELECT DISTINCT p FROM Post p 
                LEFT JOIN FETCH p.user 
                LEFT JOIN FETCH p.spot 
                LEFT JOIN FETCH p.postTags pt 
                LEFT JOIN FETCH pt.tag
                WHERE p.id IN :postIds
            """.trimIndent()
            
            entityManager.createQuery(query, Post::class.java)
                .setParameter("postIds", popularPostIds)
                .resultList
        }
        
        val postResponses = posts.map { post ->
            // 별도 쿼리로 좋아요 여부 확인
            val hasLikedQuery = """
                SELECT COUNT(pl) > 0 FROM PostLike pl 
                WHERE pl.post.id = :postId AND pl.user.id = :userId
            """.trimIndent()
            
            val hasLiked = entityManager.createQuery(hasLikedQuery, Boolean::class.java)
                .setParameter("postId", post.id)
                .setParameter("userId", user.id)
                .singleResult
                
            post.toDto().apply { this.hasLiked = hasLiked }
        }
        
        val total = if (popularPostIds.isEmpty()) {
            entityManager.createQuery("SELECT COUNT(p) FROM Post p", Long::class.java).singleResult
        } else {
            popularPostIds.size.toLong()
        }
        
        return PageImpl(postResponses, pageable, total)
    }
}