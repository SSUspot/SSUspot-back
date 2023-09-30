package com.ssuspot.sns.api.controller.post;

@org.springframework.web.bind.annotation.RestController
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J,\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0001\u0010\b\u001a\u00020\t2\b\b\u0001\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\rH\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/ssuspot/sns/api/controller/post/CommentController;", "", "commentService", "Lcom/ssuspot/sns/application/service/post/CommentService;", "(Lcom/ssuspot/sns/application/service/post/CommentService;)V", "createComment", "Lorg/springframework/http/ResponseEntity;", "Lcom/ssuspot/sns/api/response/post/CommentResponse;", "postId", "", "createCommentRequest", "Lcom/ssuspot/sns/api/request/post/CreateCommentRequest;", "userDetails", "Lcom/ssuspot/sns/infrastructure/security/UserPrincipal;", "sns"})
public class CommentController {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.post.CommentService commentService = null;
    
    public CommentController(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.post.CommentService commentService) {
        super();
    }
    
    @org.springframework.web.bind.annotation.PostMapping(value = {"/api/posts/{postId}/comments"})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.post.CommentResponse> createComment(@org.springframework.web.bind.annotation.PathVariable
    long postId, @org.springframework.web.bind.annotation.RequestBody
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.api.request.post.CreateCommentRequest createCommentRequest, @org.springframework.security.core.annotation.AuthenticationPrincipal
    @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.security.UserPrincipal userDetails) {
        return null;
    }
}