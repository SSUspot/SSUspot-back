package com.ssuspot.sns.infrastructure.configs

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils

@DisplayName("시크릿 키 설정 테스트")
class SecretKeyConfigTest {
    
    @Test
    @DisplayName("테스트 환경에서는 검증을 수행하지 않는다")
    fun validateSecrets_TestProfile_SkipsValidation() {
        // given - 테스트 환경에서는 빈 시크릿도 허용
        val config = SecretKeyConfig("", "", "test")
        
        // when & then
        assertThatCode { config.validateSecrets() }
            .doesNotThrowAnyException()
    }
    
    @Test
    @DisplayName("유효한 JWT 시크릿으로 검증이 성공한다")
    fun validateSecrets_ValidJwtSecret_Success() {
        // given
        val validSecret = "this-is-a-valid-secret-key-that-is-at-least-32-characters-long"
        val config = SecretKeyConfig(validSecret, "valid-redis-password-16chars", "default")
        
        // when & then
        assertThatCode { config.validateSecrets() }
            .doesNotThrowAnyException()
    }
    
    @Test
    @DisplayName("빈 JWT 시크릿으로 검증이 실패한다")
    fun validateSecrets_EmptyJwtSecret_ThrowsException() {
        // given
        val config = SecretKeyConfig("", "", "default")
        
        // when & then
        assertThatThrownBy { config.validateSecrets() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("JWT Secret이 설정되지 않았습니다")
    }
    
    @Test
    @DisplayName("짧은 JWT 시크릿으로 검증이 실패한다")
    fun validateSecrets_ShortJwtSecret_ThrowsException() {
        // given
        val shortSecret = "short-secret"
        val config = SecretKeyConfig(shortSecret, "", "default")
        
        // when & then
        assertThatThrownBy { config.validateSecrets() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("JWT Secret이 너무 짧습니다")
    }
    
    @Test
    @DisplayName("약한 JWT 시크릿으로 검증이 실패한다")
    fun validateSecrets_WeakJwtSecret_ThrowsException() {
        // given
        val weakSecret = "password123456789012345678901234567890"
        val config = SecretKeyConfig(weakSecret, "", "default")
        
        // when & then
        assertThatThrownBy { config.validateSecrets() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("JWT Secret이 너무 약합니다")
    }
    
    @Test
    @DisplayName("빈 Redis 비밀번호는 허용된다")
    fun validateSecrets_EmptyRedisPassword_Success() {
        // given - 개발 환경에서는 Redis 비밀번호가 비어있을 수 있음
        val validJwtSecret = "valid-jwt-secret-key-that-is-at-least-32-characters"
        val emptyRedisPassword = ""
        val config = SecretKeyConfig(validJwtSecret, emptyRedisPassword, "default")
        
        // when & then
        assertThatCode { config.validateSecrets() }
            .doesNotThrowAnyException()
    }
    
    @Test
    @DisplayName("짧은 Redis 비밀번호로 검증이 실패한다")
    fun validateSecrets_ShortRedisPassword_ThrowsException() {
        // given
        val validJwtSecret = "valid-jwt-secret-key-that-is-at-least-32-characters"
        val shortRedisPassword = "short"
        val config = SecretKeyConfig(validJwtSecret, shortRedisPassword, "default")
        
        // when & then
        assertThatThrownBy { config.validateSecrets() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Redis 비밀번호가 너무 짧습니다")
    }
    
    @Test
    @DisplayName("안전한 키 생성이 올바르게 동작한다")
    fun generateSecureKey_CreatesValidKey() {
        // when
        val key = SecretKeyConfig.generateSecureKey()
        
        // then
        assertThat(key).isNotBlank()
        assertThat(key.length).isGreaterThanOrEqualTo(43) // Base64 인코딩된 32바이트
        
        // Base64 디코딩이 가능해야 함
        val decoded = java.util.Base64.getDecoder().decode(key)
        assertThat(decoded.size).isEqualTo(32)
    }
    
    @Test
    @DisplayName("생성된 키는 매번 다르다")
    fun generateSecureKey_CreatesUniqueKeys() {
        // when
        val key1 = SecretKeyConfig.generateSecureKey()
        val key2 = SecretKeyConfig.generateSecureKey()
        val key3 = SecretKeyConfig.generateSecureKey()
        
        // then
        assertThat(key1).isNotEqualTo(key2)
        assertThat(key2).isNotEqualTo(key3)
        assertThat(key1).isNotEqualTo(key3)
    }
    
    @Test
    @DisplayName("커스텀 길이의 키 생성이 올바르게 동작한다")
    fun generateSecureKey_CustomLength_CreatesCorrectSizeKey() {
        // when
        val key16 = SecretKeyConfig.generateSecureKey(16)
        val key64 = SecretKeyConfig.generateSecureKey(64)
        
        // then
        val decoded16 = java.util.Base64.getDecoder().decode(key16)
        val decoded64 = java.util.Base64.getDecoder().decode(key64)
        
        assertThat(decoded16.size).isEqualTo(16)
        assertThat(decoded64.size).isEqualTo(64)
    }
}