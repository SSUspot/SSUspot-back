package com.ssuspot.sns.domain.model.post.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "posts")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001c\b\u0007\u0018\u00002\u00020\u0001B\u0085\u0001\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e\u0012\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u000e\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u0016R\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001e\u0010\b\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001f\u001a\u0004\b\u001d\u0010\u001eR$\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0018\"\u0004\b!\u0010\"R\u001e\u0010\n\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0018R\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0018R\u0016\u0010\u0014\u001a\u00020\u00158\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u001a\"\u0004\b,\u0010\u001cR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u001e\u0010\t\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010$\"\u0004\b0\u0010&\u00a8\u00061"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/entity/Post;", "Lcom/ssuspot/sns/domain/model/common/BaseTimeEntity;", "id", "", "user", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "title", "", "content", "viewCount", "likeCount", "imageUrls", "", "comments", "", "Lcom/ssuspot/sns/domain/model/post/entity/Comment;", "postLikes", "Lcom/ssuspot/sns/domain/model/post/entity/PostLike;", "postTags", "Lcom/ssuspot/sns/domain/model/post/entity/PostTag;", "spot", "Lcom/ssuspot/sns/domain/model/spot/entity/Spot;", "(Ljava/lang/Long;Lcom/ssuspot/sns/domain/model/user/entity/User;Ljava/lang/String;Ljava/lang/String;JJLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lcom/ssuspot/sns/domain/model/spot/entity/Spot;)V", "getComments", "()Ljava/util/List;", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getImageUrls", "setImageUrls", "(Ljava/util/List;)V", "getLikeCount", "()J", "setLikeCount", "(J)V", "getPostLikes", "getPostTags", "getSpot", "()Lcom/ssuspot/sns/domain/model/spot/entity/Spot;", "getTitle", "setTitle", "getUser", "()Lcom/ssuspot/sns/domain/model/user/entity/User;", "getViewCount", "setViewCount", "sns"})
public final class Post extends com.ssuspot.sns.domain.model.common.BaseTimeEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "user_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.user.entity.User user = null;
    @javax.persistence.Column(name = "title")
    @org.jetbrains.annotations.NotNull
    private java.lang.String title;
    @javax.persistence.Lob
    @javax.persistence.Column(name = "content")
    @org.jetbrains.annotations.NotNull
    private java.lang.String content;
    @javax.persistence.Column(name = "view_count")
    private long viewCount;
    @javax.persistence.Column(name = "like_count")
    private long likeCount;
    @javax.persistence.ElementCollection
    @org.jetbrains.annotations.NotNull
    private java.util.List<java.lang.String> imageUrls;
    @javax.persistence.OneToMany(mappedBy = "post", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.Comment> comments = null;
    @javax.persistence.OneToMany(mappedBy = "post", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostLike> postLikes = null;
    @javax.persistence.OneToMany(mappedBy = "post", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostTag> postTags = null;
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "spot_id")
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.domain.model.spot.entity.Spot spot = null;
    
    public Post(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.user.entity.User user, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String content, long viewCount, long likeCount, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> imageUrls, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.Comment> comments, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.PostLike> postLikes, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.PostTag> postTags, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.domain.model.spot.entity.Spot spot) {
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
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getContent() {
        return null;
    }
    
    public final void setContent(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final long getViewCount() {
        return 0L;
    }
    
    public final void setViewCount(long p0) {
    }
    
    public final long getLikeCount() {
        return 0L;
    }
    
    public final void setLikeCount(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getImageUrls() {
        return null;
    }
    
    public final void setImageUrls(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.Comment> getComments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostLike> getPostLikes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostTag> getPostTags() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ssuspot.sns.domain.model.spot.entity.Spot getSpot() {
        return null;
    }
}