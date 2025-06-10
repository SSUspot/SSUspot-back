package com.ssuspot.sns.infrastructure.configs

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

/**
 * 테스트용 캐시 설정
 * 간단한 인메모리 캐시 매니저 사용
 */
@TestConfiguration
@EnableCaching
class TestCacheConfig {

    @Bean
    @Primary
    fun testCacheManager(): CacheManager {
        return ConcurrentMapCacheManager(
            "posts", "post-details", "post-summaries",
            "users", "user-posts", "user-followers", 
            "spots", "spot-posts",
            "tags", "tag-posts",
            "recommended-posts", "following-posts",
            "post-stats", "user-stats"
        )
    }
}