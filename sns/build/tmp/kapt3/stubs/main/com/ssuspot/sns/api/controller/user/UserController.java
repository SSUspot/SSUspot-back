package com.ssuspot.sns.api.controller.user;

@org.springframework.web.bind.annotation.RestController
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0017J\u0018\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\b2\b\b\u0001\u0010\n\u001a\u00020\u000eH\u0017R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/ssuspot/sns/api/controller/user/UserController;", "", "userService", "Lcom/ssuspot/sns/application/service/user/UserService;", "(Lcom/ssuspot/sns/application/service/user/UserService;)V", "getUserService", "()Lcom/ssuspot/sns/application/service/user/UserService;", "login", "Lorg/springframework/http/ResponseEntity;", "Lcom/ssuspot/sns/api/response/user/LoginResponse;", "request", "Lcom/ssuspot/sns/api/request/user/LoginRequest;", "register", "Lcom/ssuspot/sns/api/response/user/RegisterResponse;", "Lcom/ssuspot/sns/api/request/user/RegisterRequest;", "sns"})
public class UserController {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.user.UserService userService = null;
    
    public UserController(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.user.UserService userService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.service.user.UserService getUserService() {
        return null;
    }
    
    @org.springframework.web.bind.annotation.PostMapping(value = {"/api/users/register"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.user.RegisterResponse> register(@org.springframework.web.bind.annotation.RequestBody
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.api.request.user.RegisterRequest request) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.PostMapping(value = {"/api/users/login"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.user.LoginResponse> login(@org.springframework.web.bind.annotation.RequestBody
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.api.request.user.LoginRequest request) {
        return null;
    }
}