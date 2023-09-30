package com.ssuspot.sns.domain.model.post.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "post_tags")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/entity/PostTag;", "", "id", "", "post", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "tag", "Lcom/ssuspot/sns/domain/model/post/entity/Tag;", "(Ljava/lang/Long;Lcom/ssuspot/sns/domain/model/post/entity/Post;Lcom/ssuspot/sns/domain/model/post/entity/Tag;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getPost", "()Lcom/ssuspot/sns/domain/model/post/entity/Post;", "getTag", "()Lcom/ssuspot/sns/domain/model/post/entity/Tag;", "sns"})
public final class PostTag {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "post_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.post.entity.Post post = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "tag_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.post.entity.Tag tag = null;
    
    public PostTag(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.post.entity.Post post, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.post.entity.Tag tag) {
        super();
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
    public final com.ssuspot.sns.domain.model.post.entity.Tag getTag() {
        return null;
    }
}