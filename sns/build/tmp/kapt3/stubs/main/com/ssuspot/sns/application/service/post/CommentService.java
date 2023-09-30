package com.ssuspot.sns.application.service.post;

@org.springframework.stereotype.Service
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0017J\f\u0010\r\u001a\u00020\n*\u00020\u000eH\u0012J\u001c\u0010\u000f\u001a\u00020\u000e*\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0092\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0092\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/ssuspot/sns/application/service/post/CommentService;", "", "commentRepository", "Lcom/ssuspot/sns/infrastructure/repository/post/CommentRepository;", "userService", "Lcom/ssuspot/sns/application/service/user/UserService;", "postService", "Lcom/ssuspot/sns/application/service/post/PostService;", "(Lcom/ssuspot/sns/infrastructure/repository/post/CommentRepository;Lcom/ssuspot/sns/application/service/user/UserService;Lcom/ssuspot/sns/application/service/post/PostService;)V", "createComment", "Lcom/ssuspot/sns/application/dto/post/CommentResponseDto;", "createCommentDto", "Lcom/ssuspot/sns/application/dto/post/CreateCommentDto;", "toDto", "Lcom/ssuspot/sns/domain/model/post/entity/Comment;", "toEntity", "post", "Lcom/ssuspot/sns/domain/model/post/entity/Post;", "user", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "sns"})
public class CommentService {
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.infrastructure.repository.post.CommentRepository commentRepository = null;
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.user.UserService userService = null;
    @org.jetbrains.annotations.NotNull
    private final com.ssuspot.sns.application.service.post.PostService postService = null;
    
    public CommentService(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.infrastructure.repository.post.CommentRepository commentRepository, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.user.UserService userService, @org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.service.post.PostService postService) {
        super();
    }
    
    @org.springframework.transaction.annotation.Transactional
    @org.jetbrains.annotations.NotNull
    public com.ssuspot.sns.application.dto.post.CommentResponseDto createComment(@org.jetbrains.annotations.NotNull
    com.ssuspot.sns.application.dto.post.CreateCommentDto createCommentDto) {
        return null;
    }
    
    private com.ssuspot.sns.domain.model.post.entity.Comment toEntity(com.ssuspot.sns.application.dto.post.CreateCommentDto $this$toEntity, com.ssuspot.sns.domain.model.post.entity.Post post, com.ssuspot.sns.domain.model.user.entity.User user) {
        return null;
    }
    
    private com.ssuspot.sns.application.dto.post.CommentResponseDto toDto(com.ssuspot.sns.domain.model.post.entity.Comment $this$toDto) {
        return null;
    }
}