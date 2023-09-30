package com.ssuspot.sns.domain.model.spot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpot is a Querydsl query type for Spot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpot extends EntityPathBase<Spot> {

    private static final long serialVersionUID = -433629130L;

    public static final QSpot spot = new QSpot("spot");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final ListPath<com.ssuspot.sns.domain.model.post.entity.Post, com.ssuspot.sns.domain.model.post.entity.QPost> posts = this.<com.ssuspot.sns.domain.model.post.entity.Post, com.ssuspot.sns.domain.model.post.entity.QPost>createList("posts", com.ssuspot.sns.domain.model.post.entity.Post.class, com.ssuspot.sns.domain.model.post.entity.QPost.class, PathInits.DIRECT2);

    public final NumberPath<Integer> spotLevel = createNumber("spotLevel", Integer.class);

    public final StringPath spotName = createString("spotName");

    public final StringPath spotThumbnailImageLink = createString("spotThumbnailImageLink");

    public QSpot(String variable) {
        super(Spot.class, forVariable(variable));
    }

    public QSpot(Path<Spot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpot(PathMetadata metadata) {
        super(Spot.class, metadata);
    }

}

