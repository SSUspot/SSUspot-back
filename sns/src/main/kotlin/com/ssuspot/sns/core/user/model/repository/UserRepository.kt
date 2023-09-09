package com.ssuspot.sns.core.user.model.repository

import com.ssuspot.sns.core.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUserName(userName: String): User?
    fun findByNickname(nickname: String): User?
}