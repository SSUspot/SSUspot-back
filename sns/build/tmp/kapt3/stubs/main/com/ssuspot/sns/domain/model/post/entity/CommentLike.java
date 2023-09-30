package com.ssuspot.sns.domain.model.post.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "comment_likes")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/entity/CommentLike;", "", "id", "", "user", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "comment", "Lcom/ssuspot/sns/domain/model/post/entity/Comment;", "(Ljava/lang/Long;Lcom/ssuspot/sns/domain/model/user/entity/User;Lcom/ssuspot/sns/domain/model/post/entity/Comment;)V", "getComment", "()Lcom/ssuspot/sns/domain/model/post/entity/Comment;", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getUser", "()Lcom/ssuspot/sns/domain/model/user/entity/User;", "sns"})
public final class CommentLike {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "user_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.user.entity.User user = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "comment_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.post.entity.Comment comment = null;
    
    public CommentLike(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.entity.User user, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.post.entity.Comment comment) {
        super();
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
    public final com.ssuspot.sns.domain.model.post.entity.Comment getComment() {
        return null;
    }
}