package com.ssuspot.sns.domain.model.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserFollow is a Querydsl query type for UserFollow
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserFollow extends EntityPathBase<UserFollow> {

    private static final long serialVersionUID = -275830247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFollow userFollow = new QUserFollow("userFollow");

    public final QUser followee;

    public final QUser follower;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QUserFollow(String variable) {
        this(UserFollow.class, forVariable(variable), INITS);
    }

    public QUserFollow(Path<UserFollow> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserFollow(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserFollow(PathMetadata metadata, PathInits inits) {
        this(UserFollow.class, metadata, inits);
    }

    public QUserFollow(Class<? extends UserFollow> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.followee = inits.isInitialized("followee") ? new QUser(forProperty("followee")) : null;
        this.follower = inits.isInitialized("follower") ? new QUser(forProperty("follower")) : null;
    }

}

