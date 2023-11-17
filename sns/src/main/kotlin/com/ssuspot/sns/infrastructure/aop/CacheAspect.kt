package com.ssuspot.sns.infrastructure.aop

import org.springframework.stereotype.Component

@Component
class CacheAspect(
    _redisTemplate: RedisTemplate,
) {

    init {
        CacheAspect.redisTemplate = _redisTemplate
    }

    companion object {
        lateinit var redisTemplate: RedisTemplate
            private set
    }
}