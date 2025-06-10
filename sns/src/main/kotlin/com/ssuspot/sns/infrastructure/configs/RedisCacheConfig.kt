package com.ssuspot.sns.infrastructure.configs

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@EnableCaching
@Primary
class RedisCacheConfig {
    
    @Bean
    fun contentCacheManager(cf: RedisConnectionFactory?): CacheManager {
        // 기본 설정
        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )
            .entryTtl(Duration.ofMinutes(5L))
            .disableCachingNullValues()
            
        // 서로 다른 TTL을 가진 캐시 설정
        val cacheConfigurations = mapOf(
            // 사용자 정보: 30분 캐시 (User 정보는 자주 변경되지 않음)
            "users" to defaultConfig.entryTtl(Duration.ofMinutes(30L)),
            "userInfo" to defaultConfig.entryTtl(Duration.ofMinutes(30L)),
            
            // 게시물 목록: 5분 캐시 (새 게시물이 자주 등록됨)
            "posts" to defaultConfig.entryTtl(Duration.ofMinutes(5L)),
            "postsBySpot" to defaultConfig.entryTtl(Duration.ofMinutes(5L)),
            "postsByUser" to defaultConfig.entryTtl(Duration.ofMinutes(10L)),
            "postsByTag" to defaultConfig.entryTtl(Duration.ofMinutes(10L)),
            "followingPosts" to defaultConfig.entryTtl(Duration.ofMinutes(3L)),
            "recommendedPosts" to defaultConfig.entryTtl(Duration.ofMinutes(10L)),
            
            // 스팟 정보: 1시간 캐시 (스팟 정보는 거의 변경되지 않음)
            "spots" to defaultConfig.entryTtl(Duration.ofHours(1L)),
            
            // 태그 정보: 30분 캐시 (태그는 거의 변경되지 않음)
            "tags" to defaultConfig.entryTtl(Duration.ofMinutes(30L)),
            
            // 댓글: 2분 캐시 (댓글은 자주 등록됨)
            "comments" to defaultConfig.entryTtl(Duration.ofMinutes(2L)),
            
            // 좋아요 수: 1분 캐시 (좋아요는 자주 변경됨)
            "likes" to defaultConfig.entryTtl(Duration.ofMinutes(1L)),
            
            // 팩로우 정보: 10분 캐시
            "follows" to defaultConfig.entryTtl(Duration.ofMinutes(10L))
        )
        
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf!!)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }
}
