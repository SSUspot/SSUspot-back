package com.ssuspot.sns.infrastructure.repository.post;

@org.springframework.stereotype.Repository
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/ssuspot/sns/infrastructure/repository/post/PostRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "", "Lcom/ssuspot/sns/domain/model/post/repository/CustomPostRepository;", "sns"})
public abstract interface PostRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ssuspot.sns.domain.model.post.entity.Post, java.lang.Long>, com.ssuspot.sns.domain.model.post.repository.CustomPostRepository {
}