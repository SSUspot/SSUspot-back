package com.ssuspot.sns.api.exception;

@org.springframework.web.bind.annotation.RestControllerAdvice
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0017J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\tH\u0017J\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u000bH\u0017J\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\rH\u0017\u00a8\u0006\u000e"}, d2 = {"Lcom/ssuspot/sns/api/exception/UserExceptionHandler;", "", "()V", "handleEmailExistException", "Lorg/springframework/http/ResponseEntity;", "Lcom/ssuspot/sns/api/response/common/ErrorResponse;", "exception", "Lcom/ssuspot/sns/domain/exceptions/user/EmailExistException;", "handleUserNotFoundException", "Lcom/ssuspot/sns/domain/exceptions/user/UserNotFoundException;", "handleUserPasswordIncorrectException", "Lcom/ssuspot/sns/domain/exceptions/user/UserPasswordIncorrectException;", "handleUserRegisterFailedException", "Lcom/ssuspot/sns/domain/exceptions/user/UserRegisterFailedException;", "sns"})
public class UserExceptionHandler {
    
    public UserExceptionHandler() {
        super();
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {com.ssuspot.sns.domain.exceptions.user.UserNotFoundException.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.common.ErrorResponse> handleUserNotFoundException(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.exceptions.user.UserNotFoundException exception) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.common.ErrorResponse> handleUserPasswordIncorrectException(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.exceptions.user.UserPasswordIncorrectException exception) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {com.ssuspot.sns.domain.exceptions.user.EmailExistException.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.common.ErrorResponse> handleEmailExistException(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.exceptions.user.EmailExistException exception) {
        return null;
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {com.ssuspot.sns.domain.exceptions.user.UserRegisterFailedException.class})
    @org.jetbrains.annotations.NotNull
    public org.springframework.http.ResponseEntity<com.ssuspot.sns.api.response.common.ErrorResponse> handleUserRegisterFailedException(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.exceptions.user.UserRegisterFailedException exception) {
        return null;
    }
}