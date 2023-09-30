package com.ssuspot.sns.infrastructure.repository.user;

@org.springframework.stereotype.Repository
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\bg\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u0006H&J\u0012\u0010\u0007\u001a\u0004\u0018\u00010\u00022\u0006\u0010\b\u001a\u00020\u0006H&J\u0012\u0010\t\u001a\u0004\u0018\u00010\u00022\u0006\u0010\n\u001a\u00020\u0006H&\u00a8\u0006\u000b"}, d2 = {"Lcom/ssuspot/sns/infrastructure/repository/user/UserRepository;", "Lorg/springframework/data/jpa/repository/JpaRepository;", "Lcom/ssuspot/sns/domain/model/user/entity/User;", "", "findByEmail", "email", "", "findByNickname", "nickname", "findByUserName", "userName", "sns"})
public abstract interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ssuspot.sns.domain.model.user.entity.User, java.lang.Long> {
    
    @org.jetbrains.annotations.Nullable
    public abstract com.ssuspot.sns.domain.model.user.entity.User findByEmail(@org.jetbrains.annotations.NotNull
    java.lang.String email);
    
    @org.jetbrains.annotations.Nullable
    public abstract com.ssuspot.sns.domain.model.user.entity.User findByUserName(@org.jetbrains.annotations.NotNull
    java.lang.String userName);
    
    @org.jetbrains.annotations.Nullable
    public abstract com.ssuspot.sns.domain.model.user.entity.User findByNickname(@org.jetbrains.annotations.NotNull
    java.lang.String nickname);
}