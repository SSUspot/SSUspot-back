package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.application.dto.post.PostSummaryDto
import com.ssuspot.sns.domain.model.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    
    /**
     * DTO 프로젝션을 사용한 게시물 목록 조회 (성능 최적화)
     */
    @Query("""
        SELECT new com.ssuspot.sns.application.dto.post.PostSummaryDto(
            p.id, p.title, p.content, p.viewCount, p.likeCount, p.rating,
            p.imageUrls, p.createdAt, p.updatedAt,
            u.id, u.nickname, u.profileImageLink,
            s.id, s.spotName,
            COUNT(c.id)
        )
        FROM Post p
        JOIN p.user u
        JOIN p.spot s
        LEFT JOIN p.comments c
        WHERE p.spot.id = :spotId
        GROUP BY p.id, p.title, p.content, p.viewCount, p.likeCount, p.rating,
                 p.imageUrls, p.createdAt, p.updatedAt,
                 u.id, u.nickname, u.profileImageLink,
                 s.id, s.spotName
        ORDER BY p.createdAt DESC
    """)
    fun findPostSummariesBySpotId(@Param("spotId") spotId: Long, pageable: Pageable): Page<PostSummaryDto>
    
    @Query("""
        SELECT new com.ssuspot.sns.application.dto.post.PostSummaryDto(
            p.id, p.title, p.content, p.viewCount, p.likeCount, p.rating,
            p.imageUrls, p.createdAt, p.updatedAt,
            u.id, u.nickname, u.profileImageLink,
            s.id, s.spotName,
            COUNT(c.id)
        )
        FROM Post p
        JOIN p.user u
        JOIN p.spot s
        LEFT JOIN p.comments c
        WHERE p.user.id = :userId
        GROUP BY p.id, p.title, p.content, p.viewCount, p.likeCount, p.rating,
                 p.imageUrls, p.createdAt, p.updatedAt,
                 u.id, u.nickname, u.profileImageLink,
                 s.id, s.spotName
        ORDER BY p.createdAt DESC
    """)
    fun findPostSummariesByUserId(@Param("userId") userId: Long, pageable: Pageable): Page<PostSummaryDto>
}
