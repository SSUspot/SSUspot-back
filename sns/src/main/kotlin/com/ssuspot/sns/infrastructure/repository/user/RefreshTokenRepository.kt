package com.ssuspot.sns.infrastructure.repository.user

import com.ssuspot.sns.domain.model.user.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findAllByEmail(email: String): List<RefreshToken>
    fun deleteAllByEmail(email: String)
}