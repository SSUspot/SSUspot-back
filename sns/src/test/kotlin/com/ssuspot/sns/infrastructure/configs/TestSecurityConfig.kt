package com.ssuspot.sns.infrastructure.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.infrastructure.security.JwtAuthenticationFilter
import com.ssuspot.sns.infrastructure.security.JwtTokenProvider
import com.ssuspot.sns.infrastructure.security.UserAccessDeniedHandler
import com.ssuspot.sns.infrastructure.security.UserAuthenticationEntryPoint
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@TestConfiguration
@Profile("test")
class TestSecurityConfig(
    private val objectMapper: ObjectMapper,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userAuthenticationEntryPoint: UserAuthenticationEntryPoint,
    private val userAccessDeniedHandler: UserAccessDeniedHandler
) {
    
    @Bean
    @Primary
    @Throws(Exception::class)
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { requests ->
                requests
                    // Public endpoints - no authentication required
                    .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users/refresh").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/posts/*/comments").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/comments/*").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/spots").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/spots").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/spots/*").permitAll()
                    
                    // Health check and actuator endpoints
                    .requestMatchers("/actuator/**").permitAll()
                    
                    // API Documentation endpoints
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/api-docs/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    
                    // Protected endpoints - authentication required
                    .requestMatchers("/api/alarms/**").authenticated()
                    .requestMatchers("/api/images/**").authenticated()
                    .requestMatchers("/api/posts/**").authenticated()
                    .requestMatchers("/api/tags/**").authenticated()
                    .requestMatchers("/api/users/**").authenticated()
                    
                    // Default: require authentication for all other endpoints
                    .anyRequest().authenticated()
            }
            // NOTE: RateLimitingFilter is intentionally excluded for test environment
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider, objectMapper),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(userAuthenticationEntryPoint)
                    .accessDeniedHandler(userAccessDeniedHandler)
            }
        return http.build()
    }

    @Bean
    fun testPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}