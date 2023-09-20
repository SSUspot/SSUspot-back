package com.ssuspot.sns.infrastructure.repository.user

import com.ssuspot.sns.domain.model.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository:JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUserName(userName: String): User?
    fun findByNickname(nickname: String): User?
}