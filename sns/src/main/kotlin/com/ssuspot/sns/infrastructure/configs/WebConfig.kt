package com.ssuspot.sns.infrastructure.configs

import com.ssuspot.sns.infrastructure.security.AuthHandlerMethodArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authHandlerMethodArgumentResolver: AuthHandlerMethodArgumentResolver
) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") //모든 요청에 대해서 CORS 설정
                // 권한 조정 필요. 허용할 origin만 설정해야하는데 일단 다 허용
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PATCH", "DELETE") // CORS 허용할 Method
    }
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authHandlerMethodArgumentResolver)
    }
}