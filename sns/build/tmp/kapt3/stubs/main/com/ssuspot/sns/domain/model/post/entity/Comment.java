package com.ssuspot.sns.domain.model.post.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "comments")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\b\u001a\u00020\tR\u001e\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0018"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/entity/Comment;", "Lcom/ssuspot/sns/domain/model/common/BaseTimeEntity;", "id", "", "post", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "user", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "content", "", "(Ljava/lang/Long;Lcom/ssuspot/sns/domain/model/post/entity/Post;Lcom/ssuspot/sns/domain/model/user/entity/User;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getPost", "()Lcom/ssuspot/sns/domain/model/post/entity/Post;", "getUser", "()Lcom/ssuspot/sns/domain/model/user/entity/User;", "update", "", "sns"})
public final class Comment extends com.ssuspot.sns.domain.model.common.BaseTimeEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "post_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.post.entity.Post post = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "user_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.user.entity.User user = null;
    @javax.persistence.Lob
    @javax.persistence.Column(name = "content")
    @org.jetbrains.annotations.NotNull
    private java.lang.String content;
    
    public Comment(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.post.entity.Post post, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.entity.User user, @org.jetbrains.annotations.NotNull
    java.lang.String content) {
        super(null, null);
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.post.entity.Post getPost() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.user.entity.User getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getContent() {
        return null;
    }
    
    public final void setContent(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final void update(@org.jetbrains.annotations.NotNull
    java.lang.String content) {
    }
}