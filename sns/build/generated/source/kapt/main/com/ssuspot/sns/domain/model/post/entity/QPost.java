package com.ssuspot.sns.domain.model.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1863064178L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.ssuspot.sns.domain.model.common.QBaseTimeEntity _super = new com.ssuspot.sns.domain.model.common.QBaseTimeEntity(this);

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> imageUrls = this.<String, StringPath>createList("imageUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final ListPath<PostLike, QPostLike> postLikes = this.<PostLike, QPostLike>createList("postLikes", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final ListPath<PostTag, QPostTag> postTags = this.<PostTag, QPostTag>createList("postTags", PostTag.class, QPostTag.class, PathInits.DIRECT2);

    public final com.ssuspot.sns.domain.model.spot.entity.QSpot spot;

    public final StringPath title = createString("title");

    //inherited
    public final NumberPath<Long> updatedAt = _super.updatedAt;

    public final com.ssuspot.sns.domain.model.user.entity.QUser user;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.spot = inits.isInitialized("spot") ? new com.ssuspot.sns.domain.model.spot.entity.QSpot(forProperty("spot")) : null;
        this.user = inits.isInitialized("user") ? new com.ssuspot.sns.domain.model.user.entity.QUser(forProperty("user")) : null;
    }

}

