package com.ssuspot.sns.domain.model.common;

@javax.persistence.MappedSuperclass
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u001d\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\r\u001a\u00020\u000eH\u0007J\b\u0010\u000f\u001a\u00020\u000eH\u0007R\"\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0010\n\u0002\u0010\n\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\"\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0010\n\u0002\u0010\n\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\t\u00a8\u0006\u0010"}, d2 = {"Lcom/ssuspot/sns/domain/model/common/BaseTimeEntity;", "", "createdAt", "", "updatedAt", "(Ljava/lang/Long;Ljava/lang/Long;)V", "getCreatedAt", "()Ljava/lang/Long;", "setCreatedAt", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getUpdatedAt", "setUpdatedAt", "onCreate", "", "onUpdate", "sns"})
public class BaseTimeEntity {
    @javax.persistence.Column(name = "created_at")
    @org.jetbrains.annotations.Nullable
    private java.lang.Long createdAt;
    @javax.persistence.Column(name = "updated_at")
    @org.jetbrains.annotations.Nullable
    private java.lang.Long updatedAt;
    
    public BaseTimeEntity(@org.jetbrains.annotations.Nullable
    java.lang.Long createdAt, @org.jetbrains.annotations.Nullable
    java.lang.Long updatedAt) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getCreatedAt() {
        return null;
    }
    
    public final void setCreatedAt(@org.jetbrains.annotations.Nullable
    java.lang.Long p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getUpdatedAt() {
        return null;
    }
    
    public final void setUpdatedAt(@org.jetbrains.annotations.Nullable
    java.lang.Long p0) {
    }
    
    @javax.persistence.PrePersist
    public final void onCreate() {
    }
    
    @javax.persistence.PreUpdate
    public final void onUpdate() {
    }
    
    public BaseTimeEntity() {
        super();
    }
}