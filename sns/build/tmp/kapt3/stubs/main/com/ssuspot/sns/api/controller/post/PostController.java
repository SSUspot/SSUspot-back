package com.ssuspot.sns.api.controller.post;

@org.springframework.web.bind.annotation.RestController
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0001\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0017J2\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\r0\u00062\b\b\u0001\u0010\u000e\u001a\u00020\u000f2\b\b\u0001\u0010\u0010\u001a\u00020\u000f2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0017J<\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\r0\u00062\b\b\u0001\u0010\u000e\u001a\u00020\u000f2\b\b\u0001\u0010\u0010\u001a\u00020\u000f2\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0014\u001a\u00020\u0015H\u0017J<\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\r0\u00062\b\b\u0001\u0010\u000e\u001a\u00020\u000f2\b\b\u0001\u0010\u0010\u001a\u00020\u000f2\b\b\u0001\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0017\u001a\u00020\u0015H\u0017J\u0018\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0001\u0010\u0019\u001a\u00020\u0015H\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ssuspot/sns/api/controller/post/PostController;", "", "postService", "Lcom/ssuspot/sns/application/service/post/PostService;", "(Lcom/ssuspot/sns/application/service/post/PostService;)V", "createPost", "Lorg/springframework/http/ResponseEntity;", "Lcom/ssuspot/sns/api/response/post/PostResponse;", "request", "Lcom/ssuspot/sns/api/request/post/CreatePostRequest;", "userDetails", "Lcom/ssuspot/sns/infrastructure/security/UserPrincipal;", "getMyPosts", "", "page", "", "size", "getPostsBySpotId", "sort", "", "spotId", "", "getPostsByUserId", "userId", "getSpecificPost", "postId", "sns"})
public class PostController {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.post.PostService postService = null;
    
    public PostController(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.post.PostService postService) {
        super();
    }
    
    @org.springframework.web.bind.annotation.GetMapping(value = {"/api/posts/{postId}"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.post.PostResponse> getSpecificPost(@org.springframework.web.bind.annotation.PathVariable
    long postId) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.GetMapping(value = {"/api/posts/users/me"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<java.util.List<com.ssuspot.sns.api.response.post.PostResponse>> getMyPosts(@org.springframework.web.bind.annotation.RequestParam(value = "page", defaultValue = "1")
    int page, @org.springframework.web.bind.annotation.RequestParam(value = "size", defaultValue = "10")
    int size, @org.springframework.security.core.annotation.AuthenticationPrincipal
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.security.UserPrincipal userDetails) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.GetMapping(value = {"/api/posts/users/{userId}"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<java.util.List<com.ssuspot.sns.api.response.post.PostResponse>> getPostsByUserId(@org.springframework.web.bind.annotation.RequestParam(value = "page", defaultValue = "1")
    int page, @org.springframework.web.bind.annotation.RequestParam(value = "size", defaultValue = "10")
    int size, @org.springframework.web.bind.annotation.RequestParam(value = "sort", defaultValue = "postId")
    @org.jetbrains.annotations.NotNull
    java.lang.String sort, @org.springframework.web.bind.annotation.PathVariable(value = "userId")
    long userId) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.GetMapping(value = {"/api/posts/spots/{spotId}"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<java.util.List<com.ssuspot.sns.api.response.post.PostResponse>> getPostsBySpotId(@org.springframework.web.bind.annotation.RequestParam(value = "page", defaultValue = "1")
    int page, @org.springframework.web.bind.annotation.RequestParam(value = "size", defaultValue = "10")
    int size, @org.springframework.web.bind.annotation.RequestParam(value = "sort", defaultValue = "postId")
    @org.jetbrains.annotations.NotNull
    java.lang.String sort, @org.springframework.web.bind.annotation.PathVariable(value = "spotId")
    long spotId) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.PostMapping(value = {"/api/posts"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.post.PostResponse> createPost(@org.springframework.web.bind.annotation.RequestBody
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.api.request.post.CreatePostRequest request, @org.springframework.security.core.annotation.AuthenticationPrincipal
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.security.UserPrincipal userDetails) {
        return null;
    }
}