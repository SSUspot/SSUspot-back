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
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            ) // Value Serializer 변경
            .entryTtl(Duration.ofMinutes(3L)) // 캐시 수명 30분
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf!!)
            .cacheDefaults(redisCacheConfiguration).build()
    }
}
