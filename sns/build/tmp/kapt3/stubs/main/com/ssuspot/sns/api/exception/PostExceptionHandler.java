package com.ssuspot.sns.api.exception;

@org.springframework.web.bind.annotation.RestControllerAdvice
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0017J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\tH\u0017\u00a8\u0006\n"}, d2 = {"Lcom/ssuspot/sns/api/exception/PostExceptionHandler;", "", "()V", "invalidTextException", "Lorg/springframework/http/ResponseEntity;", "Lcom/ssuspot/sns/api/response/common/ErrorResponse;", "exception", "Lcom/ssuspot/sns/domain/exceptions/post/InvalidTextException;", "postNotFoundException", "Lcom/ssuspot/sns/domain/exceptions/post/PostNotFoundException;", "sns"})
public class PostExceptionHandler {
    
    public PostExceptionHandler() {
        super();
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {com.ssuspot.sns.domain.exceptions.post.PostNotFoundException.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.common.ErrorResponse> postNotFoundException(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.exceptions.post.PostNotFoundException exception) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {com.ssuspot.sns.domain.exceptions.post.InvalidTextException.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.common.ErrorResponse> invalidTextException(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.exceptions.post.InvalidTextException exception) {
        return null;
    }
}