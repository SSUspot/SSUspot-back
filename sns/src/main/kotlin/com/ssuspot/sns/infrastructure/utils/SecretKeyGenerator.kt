package com.ssuspot.sns.infrastructure.utils

import java.security.SecureRandom
import java.util.Base64

/**
 * 시크릿 키 생성 유틸리티
 * 
 * 사용법:
 * ```
 * fun main() {
 *     SecretKeyGenerator.generateAndPrintKeys()
 * }
 * ```
 */
object SecretKeyGenerator {
    
    /**
     * 안전한 랜덤 키 생성
     * @param lengthInBytes 키 길이 (바이트)
     * @return Base64 인코딩된 키
     */
    fun generateSecureKey(lengthInBytes: Int = 32): String {
        val random = SecureRandom.getInstanceStrong()
        val bytes = ByteArray(lengthInBytes)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }
    
    /**
     * URL-safe Base64 인코딩된 키 생성
     * @param lengthInBytes 키 길이 (바이트)
     * @return URL-safe Base64 인코딩된 키
     */
    fun generateUrlSafeKey(lengthInBytes: Int = 32): String {
        val random = SecureRandom.getInstanceStrong()
        val bytes = ByteArray(lengthInBytes)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
    
    /**
     * 16진수 형식의 키 생성
     * @param lengthInBytes 키 길이 (바이트)
     * @return 16진수 문자열
     */
    fun generateHexKey(lengthInBytes: Int = 32): String {
        val random = SecureRandom.getInstanceStrong()
        val bytes = ByteArray(lengthInBytes)
        random.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * 환경 변수 설정을 위한 키 생성 및 출력
     */
    fun generateAndPrintKeys() {
        println("""
            |=====================================================
            |🔐 SSUSpot SNS 시크릿 키 생성기
            |=====================================================
            |
            |아래 환경 변수를 설정하세요:
            |
            |# JWT 시크릿 키 (256-bit)
            |export SSUSPOT_JWT_SECRET="${generateSecureKey(32)}"
            |
            |# Redis 비밀번호 (강력한 비밀번호)
            |export SSUSPOT_REDIS_PASSWORD="${generateUrlSafeKey(24)}"
            |
            |# Database 비밀번호 (강력한 비밀번호)
            |export SSUSPOT_SNS_DB_PASSWORD="${generateUrlSafeKey(24)}"
            |
            |# S3 액세스 키 (예시)
            |export SSUSPOT_S3_ACCESS_KEY="your-aws-access-key"
            |export SSUSPOT_S3_SECRET_KEY="your-aws-secret-key"
            |
            |=====================================================
            |⚠️  주의사항:
            |1. 이 키들을 안전한 곳에 보관하세요
            |2. 절대로 Git에 커밋하지 마세요
            |3. 프로덕션 환경에서는 AWS Secrets Manager나 
            |   HashiCorp Vault 같은 시크릿 관리 도구 사용을 권장합니다
            |=====================================================
        """.trimMargin())
    }
    
    /**
     * 특정 환경을 위한 .env 파일 템플릿 생성
     */
    fun generateEnvTemplate(): String {
        return """
            |# SSUSpot SNS 환경 변수
            |# 이 파일을 .env로 복사하고 실제 값을 입력하세요
            |# 절대로 이 파일을 Git에 커밋하지 마세요!
            |
            |# Database Configuration
            |SSUSPOT_SNS_DB_URL=jdbc:postgresql://localhost:5432/ssuspot_sns
            |SSUSPOT_SNS_DB_USERNAME=ssuspot
            |SSUSPOT_SNS_DB_PASSWORD=${generateUrlSafeKey(24)}
            |
            |# Redis Configuration
            |SSUSPOT_REDIS_HOST=localhost
            |SSUSPOT_REDIS_PORT=6379
            |SSUSPOT_REDIS_PASSWORD=${generateUrlSafeKey(24)}
            |
            |# JWT Configuration
            |SSUSPOT_JWT_SECRET=${generateSecureKey(32)}
            |
            |# AWS S3 Configuration
            |SSUSPOT_S3_BUCKET_NAME=ssuspot-sns-bucket
            |SSUSPOT_S3_ACCESS_KEY=your-access-key
            |SSUSPOT_S3_SECRET_KEY=your-secret-key
            |SSUSPOT_S3_REGION=ap-northeast-2
            |
            |# CORS Configuration (comma-separated)
            |SSUSPOT_CORS_ALLOWED_ORIGINS=http://localhost:3000,https://app.ssuspot.com
        """.trimMargin()
    }
}

// 실행 가능한 메인 함수
fun main() {
    SecretKeyGenerator.generateAndPrintKeys()
}