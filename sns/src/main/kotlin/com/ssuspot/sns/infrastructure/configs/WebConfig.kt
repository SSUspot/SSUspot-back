package com.ssuspot.sns.infrastructure.configs

import com.ssuspot.sns.infrastructure.security.AuthHandlerMethodArgumentResolver
import com.ssuspot.sns.infrastructure.logging.LoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.regex.Pattern

@Configuration
class WebConfig(
    private val authHandlerMethodArgumentResolver: AuthHandlerMethodArgumentResolver,
    private val loggingInterceptor: LoggingInterceptor,
    @Value("\${cors.allowed-origins:http://localhost:3000}") 
    private val allowedOrigins: String,
    @Value("\${cors.allowed-origin-patterns:}") 
    private val allowedOriginPatterns: String
) : WebMvcConfigurer {
    
    companion object {
        // 보안상 허용되는 헤더들만 명시
        private val ALLOWED_HEADERS = arrayOf(
            "Authorization",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        )
        
        // 보안상 허용되는 HTTP 메서드들만 명시
        private val ALLOWED_METHODS = arrayOf(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        )
        
        // 위험한 Origin 패턴 검증
        private val DANGEROUS_ORIGIN_PATTERNS = listOf(
            Pattern.compile(".*\\.(exe|bat|cmd|scr|pif|com)"),
            Pattern.compile("javascript:.*"),
            Pattern.compile("data:.*"),
            Pattern.compile(".*<script.*")
        )
    }
    
    override fun addCorsMappings(registry: CorsRegistry) {
        // 환경 변수에서 콤마로 구분된 allowed origins를 파싱
        val origins = parseAndValidateOrigins(allowedOrigins)
        val originPatterns = parseOriginPatterns(allowedOriginPatterns)
        
        val corsRegistration = registry.addMapping("/api/**")
        
        // Origin 설정
        if (origins.isNotEmpty()) {
            corsRegistration.allowedOrigins(*origins.toTypedArray())
        }
        
        // Origin Pattern 설정 (동적 도메인용)
        if (originPatterns.isNotEmpty()) {
            corsRegistration.allowedOriginPatterns(*originPatterns.toTypedArray())
        }
        
        corsRegistration
            .allowedMethods(*ALLOWED_METHODS)
            .allowedHeaders(*ALLOWED_HEADERS)
            .exposedHeaders(
                "X-RateLimit-Minute-Limit",
                "X-RateLimit-Minute-Remaining", 
                "X-RateLimit-Hour-Limit",
                "X-RateLimit-Hour-Remaining"
            )
            .allowCredentials(true)
            .maxAge(3600) // 1시간 preflight 캐시
            
        // Actuator 엔드포인트는 제한적 CORS 설정
        registry.addMapping("/actuator/**")
            .allowedOrigins("http://localhost:3000") // 개발 환경만 허용
            .allowedMethods("GET", "OPTIONS")
            .allowedHeaders("Content-Type", "Accept")
            .allowCredentials(false)
            .maxAge(1800)
    }
    
    /**
     * Origin 문자열을 파싱하고 보안 검증을 수행합니다.
     */
    private fun parseAndValidateOrigins(originsStr: String): List<String> {
        if (originsStr.isBlank()) return emptyList()
        
        return originsStr.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .filter { validateOrigin(it) }
    }
    
    /**
     * Origin Pattern 문자열을 파싱합니다.
     */
    private fun parseOriginPatterns(patternsStr: String): List<String> {
        if (patternsStr.isBlank()) return emptyList()
        
        return patternsStr.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .filter { validateOriginPattern(it) }
    }
    
    /**
     * Origin의 보안성을 검증합니다.
     */
    private fun validateOrigin(origin: String): Boolean {
        // 위험한 패턴 검사
        if (DANGEROUS_ORIGIN_PATTERNS.any { it.matcher(origin.lowercase()).matches() }) {
            return false
        }
        
        // 기본 URL 형식 검증
        if (!origin.matches(Regex("^https?://[a-zA-Z0-9.-]+(:\\d+)?$"))) {
            return false
        }
        
        return true
    }
    
    /**
     * Origin Pattern의 보안성을 검증합니다.
     */
    private fun validateOriginPattern(pattern: String): Boolean {
        // 위험한 패턴 검사
        if (DANGEROUS_ORIGIN_PATTERNS.any { it.matcher(pattern.lowercase()).matches() }) {
            return false
        }
        
        // 와일드카드가 너무 광범위하지 않은지 검사
        if (pattern == "*" || pattern == "*://*" || pattern == "**") {
            return false
        }
        
        return true
    }
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authHandlerMethodArgumentResolver)
    }
    
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
            .addPathPatterns("/api/**") // API 경로에만 적용
            .excludePathPatterns(
                "/actuator/**", // Actuator 엔드포인트 제외
                "/swagger-ui/**", // Swagger UI 제외
                "/api-docs/**" // API 문서 제외
            )
    }
}