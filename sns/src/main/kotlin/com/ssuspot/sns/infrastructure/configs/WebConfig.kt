package com.ssuspot.sns.infrastructure.configs

import com.ssuspot.sns.infrastructure.security.AuthHandlerMethodArgumentResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authHandlerMethodArgumentResolver: AuthHandlerMethodArgumentResolver,
    @Value("\${cors.allowed-origins:http://localhost:3000}") 
    private val allowedOrigins: String
) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        // 환경 변수에서 콤마로 구분된 allowed origins를 파싱
        val origins = allowedOrigins.split(",").map { it.trim() }.toTypedArray()
        
        registry.addMapping("/**")
                .allowedOrigins(*origins)
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
    }
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authHandlerMethodArgumentResolver)
    }
}