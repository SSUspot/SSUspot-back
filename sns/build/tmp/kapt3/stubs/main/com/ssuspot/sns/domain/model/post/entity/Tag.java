package com.ssuspot.sns.domain.model.post.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "tags")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\u0010\tR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/entity/Tag;", "", "id", "", "tagName", "", "postTags", "", "Lcom/ssuspot/sns/domain/model/post/entity/PostTag;", "(Ljava/lang/Long;Ljava/lang/String;Ljava/util/List;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getPostTags", "()Ljava/util/List;", "getTagName", "()Ljava/lang/String;", "sns"})
public final class Tag {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.Column(name = "tag_name")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String tagName = null;
    @javax.persistence.OneToMany(mappedBy = "tag", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostTag> postTags = null;
    
    public Tag(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    java.lang.String tagName, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.PostTag> postTags) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTagName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostTag> getPostTags() {
        return null;
    }
}