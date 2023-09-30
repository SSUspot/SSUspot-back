package com.ssuspot.sns.domain.model.user.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "user_follows")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u000e"}, d2 = {"Lcom/ssuspot/sns/domain/model/user/entity/UserFollow;", "", "id", "", "follower", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "followee", "(Ljava/lang/Long;Lcom/ssuspot/sns/domain/model/user/entity/User;Lcom/ssuspot/sns/domain/model/user/entity/User;)V", "getFollowee", "()Lcom/ssuspot/sns/domain/model/user/entity/User;", "getFollower", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "sns"})
public final class UserFollow {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "follower_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.user.entity.User follower = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "followee_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.user.entity.User followee = null;
    
    public UserFollow(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.entity.User follower, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.entity.User followee) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.user.entity.User getFollower() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.user.entity.User getFollowee() {
        return null;
    }
}