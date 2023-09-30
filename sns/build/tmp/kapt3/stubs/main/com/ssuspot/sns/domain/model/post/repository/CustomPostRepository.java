package com.ssuspot.sns.domain.model.post.repository;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u001e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u00072\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH&J\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\u00072\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH&\u00a8\u0006\r"}, d2 = {"Lcom/ssuspot/sns/domain/model/post/repository/CustomPostRepository;", "", "findPostById", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "postId", "", "findPostsBySpotId", "Lorg/springframework/data/domain/Page;", "spotId", "pageable", "Lorg/springframework/data/domain/Pageable;", "findPostsByUserId", "userId", "sns"})
public abstract interface CustomPostRepository {
    
    @org.jetbrains.annotations.Nullable
    public abstract com.ssuspot.sns.domain.model.post.entity.Post findPostById(long postId);
    
    @org.jetbrains.annotations.NotNull
    public abstract org.springframework.data.domain.Page<com.ssuspot.sns.domain.model.post.entity.Post> findPostsBySpotId(long spotId, @org.jetbrains.annotations.NotNull
    org.springframework.data.domain.Pageable pageable);
    
    @org.jetbrains.annotations.NotNull
    public abstract org.springframework.data.domain.Page<com.ssuspot.sns.domain.model.post.entity.Post> findPostsByUserId(long userId, @org.jetbrains.annotations.NotNull
    org.springframework.data.domain.Pageable pageable);
}