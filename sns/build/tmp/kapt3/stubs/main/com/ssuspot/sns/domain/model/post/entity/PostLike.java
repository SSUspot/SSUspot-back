package com.ssuspot.sns.domain.model.post.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "post_likes")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/entity/PostLike;", "Lcom/ssuspot/sns/domain/model/common/BaseTimeEntity;", "id", "", "user", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "post", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "(Ljava/lang/Long;Lcom/ssuspot/sns/domain/model/user/entity/User;Lcom/ssuspot/sns/domain/model/post/entity/Post;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getPost", "()Lcom/ssuspot/sns/domain/model/post/entity/Post;", "getUser", "()Lcom/ssuspot/sns/domain/model/user/entity/User;", "sns"})
public final class PostLike extends com.ssuspot.sns.domain.model.common.BaseTimeEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "user_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.user.entity.User user = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "post_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.post.entity.Post post = null;
    
    public PostLike(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.entity.User user, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.post.entity.Post post) {
        super(null, null);
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.user.entity.User getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.post.entity.Post getPost() {
        return null;
    }
}