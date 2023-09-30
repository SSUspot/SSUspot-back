package com.ssuspot.sns.domain.model.spot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpotLike is a Querydsl query type for SpotLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpotLike extends EntityPathBase<SpotLike> {

    private static final long serialVersionUID = 1440248045L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpotLike spotLike = new QSpotLike("spotLike");

    public final com.ssuspot.sns.domain.model.common.QBaseTimeEntity _super = new com.ssuspot.sns.domain.model.common.QBaseTimeEntity(this);

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSpot spot;

    //inherited
    public final NumberPath<Long> updatedAt = _super.updatedAt;

    public final com.ssuspot.sns.domain.model.user.entity.QUser user;

    public QSpotLike(String variable) {
        this(SpotLike.class, forVariable(variable), INITS);
    }

    public QSpotLike(Path<SpotLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpotLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpotLike(PathMetadata metadata, PathInits inits) {
        this(SpotLike.class, metadata, inits);
    }

    public QSpotLike(Class<? extends SpotLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.spot = inits.isInitialized("spot") ? new QSpot(forProperty("spot")) : null;
        this.user = inits.isInitialized("user") ? new com.ssuspot.sns.domain.model.user.entity.QUser(forProperty("user")) : null;
    }

}

