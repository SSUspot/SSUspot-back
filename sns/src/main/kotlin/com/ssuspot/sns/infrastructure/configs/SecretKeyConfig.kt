package com.ssuspot.sns.infrastructure.configs

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom
import java.util.Base64

/**
 * 시크릿 키 보안 설정 및 검증
 */
@Configuration
class SecretKeyConfig(
    @Value("\${app.jwt.secret:}") private val jwtSecret: String,
    @Value("\${spring.redis.password:}") private val redisPassword: String,
    @Value("\${spring.profiles.active:default}") private val activeProfile: String
) {
    
    companion object {
        private const val MIN_JWT_SECRET_LENGTH = 32 // 256 bits
        private const val MIN_PASSWORD_LENGTH = 16
        
        /**
         * 안전한 랜덤 시크릿 키 생성
         * @param length 키 길이 (바이트)
         * @return Base64 인코딩된 시크릿 키
         */
        fun generateSecureKey(length: Int = 32): String {
            val random = SecureRandom()
            val bytes = ByteArray(length)
            random.nextBytes(bytes)
            return Base64.getEncoder().encodeToString(bytes)
        }
    }
    
    @PostConstruct
    fun validateSecrets() {
        // 테스트 환경에서는 검증 스킵
        if (activeProfile == "test") {
            return
        }
        
        // JWT Secret 검증
        validateJwtSecret()
        
        // Redis Password 검증
        validateRedisPassword()
    }
    
    private fun validateJwtSecret() {
        if (jwtSecret.isBlank()) {
            throw IllegalStateException("""
                JWT Secret이 설정되지 않았습니다!
                
                환경 변수를 설정하세요:
                export SSUSPOT_JWT_SECRET="${generateSecureKey()}"
            """.trimIndent())
        }
        
        if (jwtSecret.length < MIN_JWT_SECRET_LENGTH) {
            throw IllegalStateException("""
                JWT Secret이 너무 짧습니다! (최소 $MIN_JWT_SECRET_LENGTH 문자 필요)
                
                새로운 시크릿 키를 생성하세요:
                export SSUSPOT_JWT_SECRET="${generateSecureKey()}"
            """.trimIndent())
        }
        
        // 약한 시크릿 키 패턴 검사
        if (isWeakSecret(jwtSecret)) {
            throw IllegalStateException("""
                JWT Secret이 너무 약합니다!
                
                보안이 강화된 시크릿 키를 사용하세요:
                export SSUSPOT_JWT_SECRET="${generateSecureKey()}"
            """.trimIndent())
        }
    }
    
    private fun validateRedisPassword() {
        if (redisPassword.isNotBlank() && redisPassword.length < MIN_PASSWORD_LENGTH) {
            throw IllegalStateException("""
                Redis 비밀번호가 너무 짧습니다! (최소 $MIN_PASSWORD_LENGTH 문자 필요)
                
                환경 변수로 설정하세요:
                export SSUSPOT_REDIS_PASSWORD="your-secure-password"
            """.trimIndent())
        }
    }
    
    private fun isWeakSecret(secret: String): Boolean {
        val weakPatterns = listOf(
            "secret", "password", "12345", "admin", "test", "demo",
            "changeme", "default", "example"
        )
        
        val lowerSecret = secret.lowercase()
        return weakPatterns.any { pattern -> lowerSecret.contains(pattern) }
    }
}