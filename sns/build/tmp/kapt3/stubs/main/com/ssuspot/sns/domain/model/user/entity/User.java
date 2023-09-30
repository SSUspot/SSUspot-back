package com.ssuspot.sns.domain.model.user.entity;

@javax.persistence.Entity
@javax.persistence.Table(name = "users", uniqueConstraints = {@javax.persistence.UniqueConstraint(name = "user_name_unique", columnNames = {"user_name"}), @javax.persistence.UniqueConstraint(name = "email_unique", columnNames = {"email"}), @javax.persistence.UniqueConstraint(name = "nickname_unique", columnNames = {"nickname"})}, indexes = {@javax.persistence.Index(name = "user_name_index", columnList = "user_name"), @javax.persistence.Index(name = "email_index", columnList = "email"), @javax.persistence.Index(name = "nickname_index", columnList = "nickname")})
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001Bu\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\f\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\f\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u0005R\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0007\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u001b\u001a\u0004\b\u0019\u0010\u001aR\u001e\u0010\b\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0016\"\u0004\b\u001d\u0010\u0018R\u001e\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0016\"\u0004\b\u001f\u0010\u0018R\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0014R\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0014R \u0010\n\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0016\"\u0004\b#\u0010\u0018R \u0010\t\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0016\"\u0004\b%\u0010\u0018R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0016\"\u0004\b\'\u0010\u0018\u00a8\u0006+"}, d2 = {"Lcom/ssuspot/sns/domain/model/user/entity/User;", "Lcom/ssuspot/sns/domain/model/common/BaseTimeEntity;", "id", "", "userName", "", "password", "email", "nickname", "profileMessage", "profileImageLink", "posts", "", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "comments", "Lcom/ssuspot/sns/domain/model/post/entity/Comment;", "postLikes", "Lcom/ssuspot/sns/domain/model/post/entity/PostLike;", "(Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getComments", "()Ljava/util/List;", "getEmail", "()Ljava/lang/String;", "setEmail", "(Ljava/lang/String;)V", "getId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getNickname", "setNickname", "getPassword", "setPassword", "getPostLikes", "getPosts", "getProfileImageLink", "setProfileImageLink", "getProfileMessage", "setProfileMessage", "getUserName", "setUserName", "updateNickname", "", "newNickname", "sns"})
public final class User extends com.ssuspot.sns.domain.model.common.BaseTimeEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long id = null;
    @javax.validation.constraints.NotNull
    @javax.persistence.Column(length = 64, name = "user_name", unique = true)
    @org.jetbrains.annotations.NotNull
    private java.lang.String userName;
    @javax.validation.constraints.NotNull
    @javax.persistence.Column(name = "password")
    @org.jetbrains.annotations.NotNull
    private java.lang.String password;
    @javax.validation.constraints.NotNull
    @javax.persistence.Column(length = 64, name = "email", unique = true)
    @org.jetbrains.annotations.NotNull
    private java.lang.String email;
    @javax.validation.constraints.NotNull
    @javax.persistence.Column(length = 64, name = "nickname")
    @org.jetbrains.annotations.NotNull
    private java.lang.String nickname;
    @javax.persistence.Column(name = "profile_message")
    @org.jetbrains.annotations.Nullable
    private java.lang.String profileMessage;
    @javax.persistence.Column(name = "profile_image_link")
    @org.jetbrains.annotations.Nullable
    private java.lang.String profileImageLink;
    @javax.persistence.OneToMany(mappedBy = "user", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> posts = null;
    @javax.persistence.OneToMany(mappedBy = "user", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.Comment> comments = null;
    @javax.persistence.OneToMany(mappedBy = "user", cascade = {javax.persistence.CascadeType.ALL})
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostLike> postLikes = null;
    
    public User(@org.jetbrains.annotations.Nullable
    java.lang.Long id, @org.jetbrains.annotations.NotNull
    java.lang.String userName, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String nickname, @org.jetbrains.annotations.Nullable
    java.lang.String profileMessage, @org.jetbrains.annotations.Nullable
    java.lang.String profileImageLink, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> posts, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.Comment> comments, @org.jetbrains.annotations.NotNull
    java.util.List<com.ssuspot.sns.domain.model.post.entity.PostLike> postLikes) {
        super(null, null);
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUserName() {
        return null;
    }
    
    public final void setUserName(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPassword() {
        return null;
    }
    
    public final void setPassword(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEmail() {
        return null;
    }
    
    public final void setEmail(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getNickname() {
        return null;
    }
    
    public final void setNickname(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getProfileMessage() {
        return null;
    }
    
    public final void setProfileMessage(@org.jetbrains.annotations.Nullable
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getProfileImageLink() {
        return null;
    }
    
    public final void setProfileImageLink(@org.jetbrains.annotations.Nullable
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.Post> getPosts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.Comment> getComments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ssuspot.sns.domain.model.post.entity.PostLike> getPostLikes() {
        return null;
    }
    
    public final void updateNickname(@org.jetbrains.annotations.NotNull
    java.lang.String newNickname) {
    }
}