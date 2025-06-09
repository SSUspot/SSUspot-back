package com.ssuspot.sns.infrastructure.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * OpenAPI 3.0 설정
 * Swagger UI를 통한 API 문서 자동 생성 및 테스트 환경 제공
 */
@Configuration
class OpenApiConfig {

    @Value("\${spring.profiles.active:default}")
    private lateinit var activeProfile: String

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(serverList())
            .addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
            .components(
                Components()
                    .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
            )
    }

    private fun apiInfo(): Info {
        return Info()
            .title("SSUSpot SNS API")
            .description("""
                ## SSUSpot SNS 백엔드 API 문서
                
                ### 주요 기능
                - 사용자 인증 및 권한 관리 (JWT)
                - 게시물 CRUD 및 좋아요/댓글 시스템
                - 팔로우 시스템
                - 스팟(장소) 기반 게시물 관리
                - 태그 시스템
                - 실시간 알림
                
                ### 인증 방법
                1. `/api/auth/login`으로 로그인
                2. 응답으로 받은 `accessToken`을 Authorization 헤더에 `Bearer {token}` 형태로 포함
                3. Refresh Token을 사용한 토큰 갱신 지원
                
                ### 개발 환경
                - Spring Boot 3.2.0
                - Kotlin
                - PostgreSQL
                - Redis
                - JWT Authentication
                
                ### 주의사항
                - 모든 시간은 UTC 기준
                - 페이징은 0부터 시작
                - Rate Limiting: 분당 60회, 시간당 1000회
            """.trimIndent())
            .version("v1.0.0")
            .contact(
                Contact()
                    .name("SSUSpot Development Team")
                    .email("dev@ssuspot.com")
                    .url("https://github.com/SSUspot/SSUspot-back")
            )
            .license(
                License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
            )
    }

    private fun serverList(): List<Server> {
        return when (activeProfile) {
            "prod" -> listOf(
                Server()
                    .url("https://api.ssuspot.com")
                    .description("Production Server"),
                Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            )
            "dev" -> listOf(
                Server()
                    .url("https://dev-api.ssuspot.com")
                    .description("Development Server"),
                Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            )
            else -> listOf(
                Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            )
        }
    }

    private fun createAPIKeyScheme(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
            .description("JWT 토큰을 입력하세요 (Bearer 접두사 없이)")
    }
}