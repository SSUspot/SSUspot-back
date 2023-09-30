package com.ssuspot.sns.application.dto.common;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\u0018\u00002\u00020\u0001B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007B%\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\rR\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/ssuspot/sns/application/dto/common/ErrorDto;", "", "httpStatus", "Lorg/springframework/http/HttpStatus;", "errCode", "", "message", "(Lorg/springframework/http/HttpStatus;Ljava/lang/String;Ljava/lang/String;)V", "timestamp", "Ljava/time/LocalDateTime;", "status", "", "error", "(Ljava/time/LocalDateTime;ILjava/lang/String;Ljava/lang/String;)V", "getError", "()Ljava/lang/String;", "getMessage", "getStatus", "()I", "getTimestamp", "()Ljava/time/LocalDateTime;", "sns"})
public final class ErrorDto {
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime timestamp = null;
    private final int status = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String message = null;
    
    public ErrorDto(@org.jetbrains.annotations.NotNull
    java.time.LocalDateTime timestamp, int status, @org.jetbrains.annotations.NotNull
    java.lang.String error, @org.jetbrains.annotations.NotNull
    java.lang.String message) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime getTimestamp() {
        return null;
    }
    
    public final int getStatus() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMessage() {
        return null;
    }
    
    public ErrorDto(@org.jetbrains.annotations.NotNull
    org.springframework.http.HttpStatus httpStatus, @org.jetbrains.annotations.NotNull
    java.lang.String errCode, @org.jetbrains.annotations.NotNull
    java.lang.String message) {
        super();
    }
}