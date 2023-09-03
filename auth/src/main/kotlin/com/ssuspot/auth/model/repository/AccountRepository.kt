package com.ssuspot.auth.model.repository

import com.ssuspot.auth.model.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUserIdAndRefreshToken(userId: String, refreshToken: String): Account?
    fun findByUserId(userId: String): Account?
    fun existsByEmail(mail: String): Boolean
    fun findByEmail(email: String): Account?
}
