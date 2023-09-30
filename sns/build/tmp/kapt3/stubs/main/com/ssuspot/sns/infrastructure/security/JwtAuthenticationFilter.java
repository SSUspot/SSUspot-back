package com.ssuspot.sns.infrastructure.security;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J \u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/ssuspot/sns/infrastructure/security/JwtAuthenticationFilter;", "Lorg/springframework/web/filter/GenericFilterBean;", "jwtTokenProvider", "Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;", "objectMapper", "Lcom/fasterxml/jackson/databind/ObjectMapper;", "(Lcom/ssuspot/sns/infrastructure/security/JwtTokenProvider;Lcom/fasterxml/jackson/databind/ObjectMapper;)V", "doFilter", "", "request", "Ljavax/servlet/ServletRequest;", "response", "Ljavax/servlet/ServletResponse;", "filterChain", "Ljavax/servlet/FilterChain;", "sendErrorMessage", "res", "Ljavax/servlet/http/HttpServletResponse;", "error", "", "message", "sns"})
public final class JwtAuthenticationFilter extends org.springframework.web.filter.GenericFilterBean {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.security.JwtTokenProvider jwtTokenProvider = null;
    @org.jetbrains.annotations.NotNull
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = null;
    
    public JwtAuthenticationFilter(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.security.JwtTokenProvider jwtTokenProvider, @org.jetbrains.annotations.NotNull
    com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        super();
    }
    
    @java.lang.Override
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class, javax.servlet.ServletException.class})
    public void doFilter(@org.jetbrains.annotations.NotNull
    javax.servlet.ServletRequest request, @org.jetbrains.annotations.NotNull
    javax.servlet.ServletResponse response, @org.jetbrains.annotations.NotNull
    javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final void sendErrorMessage(javax.servlet.http.HttpServletResponse res, java.lang.String error, java.lang.String message) throws java.io.IOException {
    }
}