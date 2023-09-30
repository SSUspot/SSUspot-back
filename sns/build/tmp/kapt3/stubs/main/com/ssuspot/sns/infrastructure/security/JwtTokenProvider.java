package com.ssuspot.sns.infrastructure.security;

@org.springframework.stereotype.Component
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B#\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0003\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0003\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003H\u0016J\u0010\u0010\r\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003H\u0016J\u0018\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0005H\u0016J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u0010\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0018\u001a\u00020\u0019H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;", "", "jwtSecret", "", "accessTokenValidMilSecond", "", "refreshTokenValidMilSecond", "(Ljava/lang/String;JJ)V", "secretKey", "Ljavax/crypto/SecretKey;", "generateAccessToken", "Lcom/ssuspot/sns/application/dto/common/JwtTokenDto;", "email", "generateRefreshToken", "generateToken", "tokenValidMilSecond", "getAuthentication", "Lorg/springframework/security/core/Authentication;", "claims", "Lio/jsonwebtoken/Claims;", "getClaimsFromToken", "token", "getUserEmailFromToken", "resolveToken", "req", "Ljavax/servlet/http/HttpServletRequest;", "sns"})
public class JwtTokenProvider {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String jwtSecret = null;
    private final long accessTokenValidMilSecond = 0L;
    private final long refreshTokenValidMilSecond = 0L;
    @org.jetbrains.annotations.NotNull
    private final javax.crypto.SecretKey secretKey = null;
    
    public JwtTokenProvider(@org.springframework.beans.factory.annotation.Value(value = "${app.jwt.secret}")
    @org.jetbrains.annotations.NotNull
    java.lang.String jwtSecret, @org.springframework.beans.factory.annotation.Value(value = "${app.jwt.accessTokenExpirationMS}")
    long accessTokenValidMilSecond, @org.springframework.beans.factory.annotation.Value(value = "${app.jwt.refreshTokenExpirationMS}")
    long refreshTokenValidMilSecond) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.common.JwtTokenDto generateAccessToken(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.common.JwtTokenDto generateRefreshToken(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.common.JwtTokenDto generateToken(@org.jetbrains.annotations.NotNull
    java.lang.String email, long tokenValidMilSecond) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public io.jsonwebtoken.Claims resolveToken(@org.jetbrains.annotations.NotNull
    javax.servlet.http.HttpServletRequest req) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public io.jsonwebtoken.Claims getClaimsFromToken(@org.jetbrains.annotations.NotNull
    java.lang.String token) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public org.springframework.security.core.Authentication getAuthentication(@org.jetbrains.annotations.NotNull
    io.jsonwebtoken.Claims claims) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String getUserEmailFromToken(@org.jetbrains.annotations.NotNull
    java.lang.String token) {
        return null;
    }
}