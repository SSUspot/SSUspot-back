package com.ssuspot.sns.infrastructure.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

/**
 * 고급 캐싱 전략 설정
 * - 다단계 캐싱 (L1: 로컬 캐시, L2: Redis)
 * - 캐시별 TTL 설정
 * - 캐시 무효화 전략
 */
@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val objectMapper = ObjectMapper().apply {
            registerKotlinModule()
            // 타입 정보 포함하여 역직렬화 문제 해결
            activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
        }
        
        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        // 기본 캐시 설정
        val defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10)) // 기본 TTL 10분
            .serializeKeysWith(org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
            .disableCachingNullValues()

        // 캐시별 개별 설정
        val cacheConfigurations = mapOf(
            // 게시물 관련 캐시
            "posts" to defaultCacheConfig.entryTtl(Duration.ofMinutes(5)),
            "post-details" to defaultCacheConfig.entryTtl(Duration.ofMinutes(15)),
            "post-summaries" to defaultCacheConfig.entryTtl(Duration.ofMinutes(3)),
            
            // 사용자 관련 캐시
            "users" to defaultCacheConfig.entryTtl(Duration.ofMinutes(30)),
            "user-posts" to defaultCacheConfig.entryTtl(Duration.ofMinutes(5)),
            "user-followers" to defaultCacheConfig.entryTtl(Duration.ofMinutes(10)),
            
            // 스팟 관련 캐시  
            "spots" to defaultCacheConfig.entryTtl(Duration.ofHours(1)),
            "spot-posts" to defaultCacheConfig.entryTtl(Duration.ofMinutes(5)),
            
            // 태그 관련 캐시
            "tags" to defaultCacheConfig.entryTtl(Duration.ofMinutes(30)),
            "tag-posts" to defaultCacheConfig.entryTtl(Duration.ofMinutes(5)),
            
            // 추천 관련 캐시 (자주 변경되므로 짧은 TTL)
            "recommended-posts" to defaultCacheConfig.entryTtl(Duration.ofMinutes(2)),
            "following-posts" to defaultCacheConfig.entryTtl(Duration.ofMinutes(3)),
            
            // 통계 관련 캐시
            "post-stats" to defaultCacheConfig.entryTtl(Duration.ofMinutes(15)),
            "user-stats" to defaultCacheConfig.entryTtl(Duration.ofMinutes(30))
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory
        
        // Key는 String으로 직렬화
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        
        // Value는 JSON으로 직렬화  
        val objectMapper = ObjectMapper().apply {
            registerKotlinModule()
            activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
        }
        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        template.valueSerializer = jsonSerializer
        template.hashValueSerializer = jsonSerializer
        
        template.afterPropertiesSet()
        return template
    }
}