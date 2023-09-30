package com.ssuspot.sns.infrastructure.repository.post;

@org.springframework.stereotype.Repository
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0017\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rH\u0016J\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\n2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/ssuspot/sns/infrastructure/repository/post/CustomPostRepositoryImpl;", "Lcom/ssuspot/sns/domain/model/post/repository/CustomPostRepository;", "queryFactory", "Lcom/querydsl/jpa/impl/JPAQueryFactory;", "(Lcom/querydsl/jpa/impl/JPAQueryFactory;)V", "findPostById", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "postId", "", "findPostsBySpotId", "Lorg/springframework/data/domain/Page;", "spotId", "pageable", "Lorg/springframework/data/domain/Pageable;", "findPostsByUserId", "userId", "sns"})
public class CustomPostRepositoryImpl implements com.ssuspot.sns.domain.model.post.repository.CustomPostRepository {
    @org.jetbrains.annotations.NotNull
    private final com.querydsl.jpa.impl.JPAQueryFactory queryFactory = null;
    
    public CustomPostRepositoryImpl(@org.jetbrains.annotations.NotNull
    com.querydsl.jpa.impl.JPAQueryFactory queryFactory) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public com.ssuspot.sns.domain.model.post.entity.Post findPostById(long postId) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public org.springframework.data.domain.Page<com.ssuspot.sns.domain.model.post.entity.Post> findPostsBySpotId(long spotId, @org.jetbrains.annotations.NotNull
    org.springframework.data.domain.Pageable pageable) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public org.springframework.data.domain.Page<com.ssuspot.sns.domain.model.post.entity.Post> findPostsByUserId(long userId, @org.jetbrains.annotations.NotNull
    org.springframework.data.domain.Pageable pageable) {
        return null;
    }
}