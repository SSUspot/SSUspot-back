package com.ssuspot.sns.application.service.user;

@org.springframework.stereotype.Service
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0017\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0012J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J\u0010\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0017J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0014H\u0017R\u000e\u0010\b\u001a\u00020\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001f"}, d2 = {"Lcom/ssuspot/sns/application/service/user/UserService;", "", "userRepository", "Lcom/ssuspot/sns/infrastructure/repository/user/UserRepository;", "passwordEncoder", "Lorg/springframework/security/crypto/password/PasswordEncoder;", "jwtTokenProvider", "Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;", "applicationEventPublisher", "Lorg/springframework/context/ApplicationEventPublisher;", "(Lcom/ssuspot/sns/infrastructure/repository/user/UserRepository;Lorg/springframework/security/crypto/password/PasswordEncoder;Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;Lorg/springframework/context/ApplicationEventPublisher;)V", "getUserRepository", "()Lcom/ssuspot/sns/infrastructure/repository/user/UserRepository;", "createUser", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "registerDto", "Lcom/ssuspot/sns/application/dto/user/RegisterDto;", "deleteRefreshToken", "", "email", "", "findValidUserByEmail", "getRefreshToken", "login", "Lcom/ssuspot/sns/application/dto/user/AuthTokenDto;", "loginDto", "Lcom/ssuspot/sns/application/dto/user/LoginDto;", "registerProcess", "Lcom/ssuspot/sns/application/dto/user/RegisterResponseDto;", "saveRefreshToken", "refreshToken", "sns"})
public class UserService {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.repository.user.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = null;
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.security.JwtTokenProvider jwtTokenProvider = null;
    @org.jetbrains.annotations.NotNull
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher = null;
    
    public UserService(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.repository.user.UserRepository userRepository, @org.jetbrains.annotations.NotNull
    org.springframework.security.crypto.password.PasswordEncoder passwordEncoder, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.security.JwtTokenProvider jwtTokenProvider, @org.jetbrains.annotations.NotNull
    org.springframework.context.ApplicationEventPublisher applicationEventPublisher) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.infrastructure.repository.user.UserRepository getUserRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.user.RegisterResponseDto registerProcess(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.user.RegisterDto registerDto) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.domain.model.user.entity.User findValidUserByEmail(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.user.AuthTokenDto login(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.user.LoginDto loginDto) {
        return null;
    }
    
    @org.springframework.cache.annotation.CachePut(value = {"refreshToken"}, key = "#email")
    @org.jetbrains.annotations.NotNull
    public java.lang.String saveRefreshToken(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String refreshToken) {
        return null;
    }
    
    @org.springframework.cache.annotation.Cacheable(value = {"refreshToken"}, key = "#email")
    @org.jetbrains.annotations.Nullable
    public java.lang.String getRefreshToken(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return null;
    }
    
    @org.springframework.cache.annotation.CacheEvict(value = {"refreshToken"}, key = "#email")
    public void deleteRefreshToken(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
    }
    
    private com.ssuspot.sns.domain.model.user.entity.User createUser(com.ssuspot.sns.application.dto.user.RegisterDto registerDto) {
        return null;
    }
}