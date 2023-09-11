package com.ssuspot.sns.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.sns.security.JwtAuthenticationFilter
import com.ssuspot.sns.security.JwtTokenProvider
import javax.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
        private val jwtTokenProvider: JwtTokenProvider,
        private val objectMapper: ObjectMapper,
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }.csrf { it.disable() }.cors { }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }.authorizeHttpRequests {
                    it.requestMatchers(
                            AntPathRequestMatcher("/**") //개발을 위해 모두 허용
                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/api/user/**") //user관련 기능은 모두 permit
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/swagger-ui/**")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/swagger-ui")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/swagger-resources/**")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/v3/api-docs/**")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/v3/api-docs")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/webjars/**")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/api-docs/**")
//                    ).permitAll()
//                    it.requestMatchers(
//                            AntPathRequestMatcher("/api/weather_info/**")
//                    ).permitAll()
//                    it.requestMatchers(AntPathRequestMatcher("/api/weather/**")).permitAll()
//                    it.anyRequest().authenticated()
                }.exceptionHandling {
                    it.authenticationEntryPoint { request, response, authException ->
                        response.status = HttpServletResponse.SC_UNAUTHORIZED
                        val writer = response.writer
                        writer.write("{\"error\": \"Unauthorized: " + authException.message + "\"}")
                    }
                    it.accessDeniedHandler(AccessDeniedHandler { request, response, _ ->
                        response.status = HttpServletResponse.SC_FORBIDDEN
                        val writer = response.writer
                        writer.write("{\"error\": \"Access Denied: You must be authenticated to access this resource.\"}")
                    })
                }.addFilterBefore(
                        JwtAuthenticationFilter(jwtTokenProvider, objectMapper),
                        UsernamePasswordAuthenticationFilter::class.java
                )

        return http.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtTokenProvider, objectMapper)
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}