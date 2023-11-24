package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.user.entity.UserFollow
import org.springframework.data.jpa.repository.JpaRepository

interface UserFollowRepository: JpaRepository<UserFollow, Long>{
    fun findByFollowingUserAndFollowedUser(followingUser: User, followedUser: User): UserFollow?
}