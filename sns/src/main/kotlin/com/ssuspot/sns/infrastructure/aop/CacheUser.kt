package com.ssuspot.sns.infrastructure.aop

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class CacheUser(
    _advice: CacheUserAdvice
) {
    init {
        advice = _advice
    }

    companion object {
        lateinit var advice: CacheUserAdvice
        private const val TOKEN = "::"

        // 가변인자를 사용하여 여러개의 Any 타입의 keys 인자를 받음
        fun <T> cache(vararg keys:Any, function: () -> T): T{
            return advice.cache(generateKey(keys), function)
        }
        fun <T> evict(vararg keys:Any, function: () -> T): T{
            return advice.evict(generateKey(keys), function)
        }
        // 키 생성 규칙
        private fun generateKey(keys: Array<out Any>) = keys.joinToString(TOKEN)
    }

    @Component
    class CacheUserAdvice {
        companion object {
            private const val CACHE_NAME ="USER"
        }

        @Cacheable(value = [CACHE_NAME], key = "#key")
        fun <T> cache(key: String, function: () -> T): T {
            return function.invoke()
        }

        @CacheEvict(value = [CACHE_NAME], key = "#key")
        fun <T> evict(key: String, function: () -> T): T{
            return function.invoke()
        }
    }
}