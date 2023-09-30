package com.ssuspot.sns.infrastructure.configs;

@org.springframework.context.annotation.Configuration
@org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity(prePostEnabled = true)
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0017J\b\u0010\u000b\u001a\u00020\fH\u0017J\b\u0010\r\u001a\u00020\u000eH\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ssuspot/sns/infrastructure/configs/SecurityConfig;", "", "jwtTokenProvider", "Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;", "objectMapper", "Lcom/fasterxml/jackson/databind/ObjectMapper;", "(Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;Lcom/fasterxml/jackson/databind/ObjectMapper;)V", "filterChain", "Lorg/springframework/security/web/SecurityFilterChain;", "http", "Lorg/springframework/security/config/annotation/web/builders/HttpSecurity;", "jwtAuthenticationFilter", "Lcom/ssuspot/sns/infrastructure/security/JwtAuthenticationFilter;", "passwordEncoder", "Lorg/springframework/security/crypto/bcrypt/BCryptPasswordEncoder;", "sns"})
public class SecurityConfig {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.security.JwtTokenProvider jwtTokenProvider = null;
    @org.jetbrains.annotations.NotNull
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = null;
    
    public SecurityConfig(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.security.JwtTokenProvider jwtTokenProvider, @org.jetbrains.annotations.NotNull
    com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        super();
    }
    
    @org.springframework.context.annotation.Bean
    @kotlin.jvm.Throws(exceptionClasses = {java.lang.Exception.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.security.web.SecurityFilterChain filterChain(@org.jetbrains.annotations.NotNull
    org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws java.lang.Exception {
        return null;
    }
    
    @org.springframework.context.annotation.Bean
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.infrastructure.security.JwtAuthenticationFilter jwtAuthenticationFilter() {
        return null;
    }
    
    @org.springframework.context.annotation.Bean
    @org.jetbrains.annotations.NotNull
    public org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder() {
        return null;
    }
}