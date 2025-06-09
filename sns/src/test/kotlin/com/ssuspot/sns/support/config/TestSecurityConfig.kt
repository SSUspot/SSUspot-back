package com.ssuspot.sns.support.config

import com.ssuspot.sns.infrastructure.security.AuthInfo
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.ActiveProfiles

@TestConfiguration
@EnableWebSecurity
@ActiveProfiles("test")
class TestSecurityConfig {
    
    @Bean
    @Primary
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
        
        return http.build()
    }
    
    @Bean
    fun testPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    
    @Bean
    fun testUserDetailsService(): UserDetailsService {
        val testUser = User.builder()
            .username("test@example.com")
            .password(testPasswordEncoder().encode("password"))
            .authorities(SimpleGrantedAuthority("ROLE_USER"))
            .build()
        
        return InMemoryUserDetailsManager(testUser)
    }
}

/**
 * Mock AuthInfo for testing
 */
fun createTestAuthInfo(
    email: String = "test@example.com"
): AuthInfo {
    return AuthInfo(email)
}

/**
 * Extension function to create WithMockUser annotation programmatically
 */
annotation class WithMockTestUser(
    val email: String = "test@example.com"
)