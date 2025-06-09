package com.ssuspot.sns.application.dto.post

import java.time.LocalDateTime

/**
 * 게시물 목록 조회용 경량 DTO
 * 필요한 필드만 포함하여 성능 최적화
 */
data class PostSummaryDto(
    val id: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val likeCount: Long,
    val rating: Double?,
    val imageUrls: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    
    // User 정보 - 필요한 필드만
    val userId: Long,
    val userNickname: String,
    val userProfileImageLink: String?,
    
    // Spot 정보 - 필요한 필드만
    val spotId: Long,
    val spotName: String,
    
    // 좋아요 여부
    var hasLiked: Boolean = false,
    
    // 댓글 수
    val commentCount: Long = 0
) {
    constructor(
        id: Long,
        title: String,
        content: String,
        viewCount: Long,
        likeCount: Long,
        rating: Double?,
        imageUrls: List<String>,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime,
        userId: Long,
        userNickname: String,
        userProfileImageLink: String?,
        spotId: Long,
        spotName: String,
        commentCount: Long
    ) : this(
        id, title, content, viewCount, likeCount, rating, imageUrls,
        createdAt, updatedAt, userId, userNickname, userProfileImageLink,
        spotId, spotName, false, commentCount
    )
}