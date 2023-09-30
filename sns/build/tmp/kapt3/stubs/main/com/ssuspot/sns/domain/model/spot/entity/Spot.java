package com.ssuspot.sns.domain.model.spot.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "spots")
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0016\b\u0007\u0018\u00002\u00020\u0001BI\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\u0002\u0010\u000fR\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\t\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0016\u0010\u000b\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R$\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001e\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001e\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u001f\"\u0004\b#\u0010!\u00a8\u0006$"}, d2 = {"Lcom/ssuspot/sns/domain/model/spot/entity/Spot;", "", "id", "", "spotName", "", "spotThumbnailImageLink", "spotLevel", "", "latitude", "", "longitude", "posts", "", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "(Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;IDDLjava/util/List;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getLatitude", "()D", "getLongitude", "getPosts", "()Ljava/util/List;", "setPosts", "(Ljava/util/List;)V", "getSpotLevel", "()I", "setSpotLevel", "(I)V", "getSpotName", "()Ljava/lang/String;", "setSpotName", "(Ljava/lang/String;)V", "getSpotThumbnailImageLink", "setSpotThumbnailImageLink", "sns"})
public final class Spot {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.persistence.Column(name = "spot_name")
    @org.jetbrains.annotations.NotNull
    private java.lang.String spotName;
    @javax.persistence.Column(name = "spot_thumbnail_image_link")
    @org.jetbrains.annotations.NotNull
    private java.lang.String spotThumbnailImageLink;
    @javax.persistence.Column(name = "spot_level")
    private int spotLevel;
    @javax.persistence.Column(name = "latitude")
    private final double latitude = 0.0;
    @javax.persistence.Column(name = "longitude")
    private final double longitude = 0.0;
    @javax.persistence.OneToMany(mappedBy = "spot", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> posts;
    
    public Spot(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    java.lang.String spotName, @org.jetbrains.annotations.NotNull
    java.lang.String spotThumbnailImageLink, int spotLevel, double latitude, double longitude, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> posts) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSpotName() {
        return null;
    }
    
    public final void setSpotName(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSpotThumbnailImageLink() {
        return null;
    }
    
    public final void setSpotThumbnailImageLink(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final int getSpotLevel() {
        return 0;
    }
    
    public final void setSpotLevel(int p0) {
    }
    
    public final double getLatitude() {
        return 0.0;
    }
    
    public final double getLongitude() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> getPosts() {
        return null;
    }
    
    public final void setPosts(@org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> p0) {
    }
}