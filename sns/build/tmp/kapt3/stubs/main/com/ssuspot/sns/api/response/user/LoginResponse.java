package com.ssuspot.sns.api.response.user;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B%\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\b\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0012\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\bH\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\bH\u00c6\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\n\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000f\u00a8\u0006\u001d"}, d2 = {"Lcom/ssuspot/sns/api/response/user/LoginResponse;", "", "token", "Lcom/ssuspot/sns/application/dto/user/AuthTokenDto;", "(Lcom/ssuspot/sns/application/dto/user/AuthTokenDto;)V", "accessToken", "", "accessTokenExpiredIn", "", "refreshToken", "refreshTokenExpiredIn", "(Ljava/lang/String;JLjava/lang/String;J)V", "getAccessToken", "()Ljava/lang/String;", "getAccessTokenExpiredIn", "()J", "getRefreshToken", "getRefreshTokenExpiredIn", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "sns"})
public final class LoginResponse {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String accessToken = null;
    private final long accessTokenExpiredIn = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String refreshToken = null;
    private final long refreshTokenExpiredIn = 0L;
    
    public LoginResponse(@org.jetbrains.annotations.NotNull
    java.lang.String accessToken, long accessTokenExpiredIn, @org.jetbrains.annotations.NotNull
    java.lang.String refreshToken, long refreshTokenExpiredIn) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAccessToken() {
        return null;
    }
    
    public final long getAccessTokenExpiredIn() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRefreshToken() {
        return null;
    }
    
    public final long getRefreshTokenExpiredIn() {
        return 0L;
    }
    
    public LoginResponse(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.user.AuthTokenDto token) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.api.response.user.LoginResponse copy(@org.jetbrains.annotations.NotNull
    java.lang.String accessToken, long accessTokenExpiredIn, @org.jetbrains.annotations.NotNull
    java.lang.String refreshToken, long refreshTokenExpiredIn) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}