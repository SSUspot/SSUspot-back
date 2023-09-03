package com.ssuspot.auth.configs

import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class FeignConfig(
        @Value("\${server.internal.token}")
        val serverToken: String,
        val objectMapper: ObjectMapper
) {
    @Bean
    open fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { requestTemplate -> requestTemplate.header("Authorization", "Bearer $serverToken") }
    }

    @Bean
    open fun errorDecoder(): ErrorDecoder {
        return FeignErrorDecoder(objectMapper)
    }
}
