package com.ssuspot.sns.domain.model.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 120477768L;

    public static final QUser user = new QUser("user");

    public final com.ssuspot.sns.domain.model.common.QBaseTimeEntity _super = new com.ssuspot.sns.domain.model.common.QBaseTimeEntity(this);

    public final ListPath<com.ssuspot.sns.domain.model.post.entity.Comment, com.ssuspot.sns.domain.model.post.entity.QComment> comments = this.<com.ssuspot.sns.domain.model.post.entity.Comment, com.ssuspot.sns.domain.model.post.entity.QComment>createList("comments", com.ssuspot.sns.domain.model.post.entity.Comment.class, com.ssuspot.sns.domain.model.post.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.ssuspot.sns.domain.model.post.entity.PostLike, com.ssuspot.sns.domain.model.post.entity.QPostLike> postLikes = this.<com.ssuspot.sns.domain.model.post.entity.PostLike, com.ssuspot.sns.domain.model.post.entity.QPostLike>createList("postLikes", com.ssuspot.sns.domain.model.post.entity.PostLike.class, com.ssuspot.sns.domain.model.post.entity.QPostLike.class, PathInits.DIRECT2);

    public final ListPath<com.ssuspot.sns.domain.model.post.entity.Post, com.ssuspot.sns.domain.model.post.entity.QPost> posts = this.<com.ssuspot.sns.domain.model.post.entity.Post, com.ssuspot.sns.domain.model.post.entity.QPost>createList("posts", com.ssuspot.sns.domain.model.post.entity.Post.class, com.ssuspot.sns.domain.model.post.entity.QPost.class, PathInits.DIRECT2);

    public final StringPath profileImageLink = createString("profileImageLink");

    public final StringPath profileMessage = createString("profileMessage");

    //inherited
    public final NumberPath<Long> updatedAt = _super.updatedAt;

    public final StringPath userName = createString("userName");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

