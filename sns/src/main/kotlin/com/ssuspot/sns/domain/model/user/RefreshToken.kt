package com.ssuspot.sns.domain.model.user

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.util.concurrent.TimeUnit

/**
 * Redis에 저장되는 Refresh Token 엔티티
 * @property id Refresh Token 값 (UUID 형태)
 * @property email 사용자 이메일
 * @property ttl 만료 시간 (초 단위)
 * @property isRevoked 토큰 폐기 여부
 * @property createdAt 생성 시간
 * @property usageCount 사용 횟수 (재사용 공격 방지)
 */
@RedisHash("refresh_token")
data class RefreshToken(
    @Id
    val id: String,
    
    @Indexed
    val email: String,
    
    @TimeToLive(unit = TimeUnit.SECONDS)
    val ttl: Long,
    
    val isRevoked: Boolean = false,
    
    val createdAt: Long = System.currentTimeMillis(),
    
    val usageCount: Int = 0
) {
    fun isExpired(): Boolean {
        return System.currentTimeMillis() > createdAt + (ttl * 1000)
    }
    
    fun incrementUsage(): RefreshToken {
        return this.copy(usageCount = this.usageCount + 1)
    }
    
    fun revoke(): RefreshToken {
        return this.copy(isRevoked = true)
    }
}