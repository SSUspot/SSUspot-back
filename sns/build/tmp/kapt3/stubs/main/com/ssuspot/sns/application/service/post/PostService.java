package com.ssuspot.sns.application.service.post;

@org.springframework.stereotype.Service
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0017J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0017J\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J\u0010\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0017J&\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0017J\u0016\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0013\u001a\u00020\u001cH\u0017J\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0012J\f\u0010\u001f\u001a\u00020\n*\u00020\u000eH\u0012J\u001c\u0010 \u001a\u00020\u000e*\u00020\f2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/ssuspot/sns/application/service/post/PostService;", "", "postRepository", "Lcom/ssuspot/sns/infrastructure/repository/post/PostRepository;", "spotService", "Lcom/ssuspot/sns/application/service/spot/SpotService;", "userService", "Lcom/ssuspot/sns/application/service/user/UserService;", "(Lcom/ssuspot/sns/infrastructure/repository/post/PostRepository;Lcom/ssuspot/sns/application/service/spot/SpotService;Lcom/ssuspot/sns/application/service/user/UserService;)V", "createPost", "Lcom/ssuspot/sns/application/dto/post/PostResponseDto;", "createPostDto", "Lcom/ssuspot/sns/application/dto/post/CreatePostDto;", "findValidPostById", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "postId", "", "getMyPosts", "", "getPostsRequest", "Lcom/ssuspot/sns/application/dto/post/GetMyPostsDto;", "getPostById", "getPostsBySpotId", "spotId", "page", "", "size", "getPostsByUserId", "Lcom/ssuspot/sns/application/dto/post/GetUserPostsDto;", "toPageableLatestSort", "Lorg/springframework/data/domain/PageRequest;", "toDto", "toEntity", "spot", "Lcom/ssuspot/sns/domain/model/spot/entity/Spot;", "user", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "sns"})
public class PostService {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.repository.post.PostRepository postRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.spot.SpotService spotService = null;
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.user.UserService userService = null;
    
    public PostService(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.repository.post.PostRepository postRepository, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.spot.SpotService spotService, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.user.UserService userService) {
        super();
    }
    
    @org.springframework.transaction.annotation.Transactional
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.post.PostResponseDto createPost(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.post.CreatePostDto createPostDto) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.ssuspot.sns.application.dto.post.PostResponseDto> getMyPosts(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.post.GetMyPostsDto getPostsRequest) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.post.PostResponseDto getPostById(long postId) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.ssuspot.sns.application.dto.post.PostResponseDto> getPostsBySpotId(long spotId, int page, int size) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.ssuspot.sns.application.dto.post.PostResponseDto> getPostsByUserId(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.post.GetUserPostsDto getPostsRequest) {
        return null;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.domain.model.post.entity.Post findValidPostById(long postId) {
        return null;
    }
    
    private com.ssuspot.sns.domain.model.post.entity.Post toEntity(com.ssuspot.sns.application.dto.post.CreatePostDto $this$toEntity, com.ssuspot.sns.domain.model.spot.entity.Spot spot, com.ssuspot.sns.domain.model.user.entity.User user) {
        return null;
    }
    
    private com.ssuspot.sns.application.dto.post.PostResponseDto toDto(com.ssuspot.sns.domain.model.post.entity.Post $this$toDto) {
        return null;
    }
    
    private org.springframework.data.domain.PageRequest toPageableLatestSort(int page, int size) {
        return null;
    }
}