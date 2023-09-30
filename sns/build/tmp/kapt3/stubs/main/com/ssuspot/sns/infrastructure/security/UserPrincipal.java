package com.ssuspot.sns.infrastructure.security;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\b\u0010\b\u001a\u00020\u0003H\u0016J\b\u0010\t\u001a\u00020\u0003H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0016J\b\u0010\r\u001a\u00020\u000bH\u0016J\b\u0010\u000e\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ssuspot/sns/infrastructure/security/UserPrincipal;", "Lorg/springframework/security/core/userdetails/UserDetails;", "email", "", "(Ljava/lang/String;)V", "getAuthorities", "", "Lorg/springframework/security/core/GrantedAuthority;", "getPassword", "getUsername", "isAccountNonExpired", "", "isAccountNonLocked", "isCredentialsNonExpired", "isEnabled", "sns"})
public final class UserPrincipal implements org.springframework.security.core.userdetails.UserDetails {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String email = null;
    
    public UserPrincipal(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String getPassword() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String getUsername() {
        return null;
    }
    
    @java.lang.Override
    public boolean isEnabled() {
        return false;
    }
    
    @java.lang.Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    
    @java.lang.Override
    public boolean isAccountNonLocked() {
        return false;
    }
    
    @java.lang.Override
    public boolean isAccountNonExpired() {
        return false;
    }
}