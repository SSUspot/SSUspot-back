package com.ssuspot.sns.domain.model.notification.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "notifications")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B3\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0016\u001a\u00020\u0017R\u001e\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013\u00a8\u0006\u0018"}, d2 = {"Lcom/ssuspot/sns/domain/model/notification/entity/Notification;", "Lcom/ssuspot/sns/domain/model/common/BaseTimeEntity;", "id", "", "title", "", "link", "message", "checked", "", "(Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "getChecked", "()Z", "setChecked", "(Z)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getLink", "()Ljava/lang/String;", "getMessage", "getTitle", "read", "", "sns"})
public final class Notification extends com.ssuspot.sns.domain.model.common.BaseTimeEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.Column(name = "title")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @javax.persistence.Column(name = "link")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String link = null;
    @javax.persistence.Column(name = "message")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String message = null;
    @javax.persistence.Column(name = "checked")
    private boolean checked;
    
    public Notification(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String link, @org.jetbrains.annotations.NotNull
    java.lang.String message, boolean checked) {
        super(null, null);
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLink() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMessage() {
        return null;
    }
    
    public final boolean getChecked() {
        return false;
    }
    
    public final void setChecked(boolean p0) {
    }
    
    public final void read() {
    }
}