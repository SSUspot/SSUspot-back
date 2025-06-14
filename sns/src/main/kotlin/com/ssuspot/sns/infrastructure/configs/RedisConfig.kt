package com.ssuspot.sns.infrastructure.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableRedisRepositories
class RedisConfig {
    @Value("\${spring.redis.host}")
    private val redisHost: String? = null

    @Value("\${spring.redis.port}")
    private val redisPort = 0

    @Value("\${spring.redis.password}")
    private val password: String? = null

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisConfiguration = RedisStandaloneConfiguration()
        redisConfiguration.hostName = redisHost!!
        redisConfiguration.port = redisPort
        redisConfiguration.setPassword(password)
        return LettuceConnectionFactory(redisConfiguration)
    }

    // redisTemplate은 CacheConfig에서 정의하므로 여기서는 제거
    // 필요한 경우 다른 이름의 특수 목적 템플릿 정의 가능
}