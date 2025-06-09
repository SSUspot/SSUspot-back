package com.ssuspot.sns.domain.model.user.entity

import com.ssuspot.sns.application.dto.user.FollowUserResponseDto
import com.ssuspot.sns.domain.model.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_follows")
class UserFollow(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "following_user_id")
    val followingUser: User,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "followed_user_id")
    val followedUser: User
): BaseTimeEntity() {
    fun toDto(): FollowUserResponseDto =
        FollowUserResponseDto(
            id = id!!,
            userId = followedUser.id!!,
            nickname = followedUser.nickname,
            userName = followedUser.userName,
            profileImageLink = followedUser.profileImageLink
        )
}