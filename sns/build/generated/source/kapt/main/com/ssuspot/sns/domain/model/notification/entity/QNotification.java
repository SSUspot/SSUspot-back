package com.ssuspot.sns.domain.model.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -922398584L;

    public static final QNotification notification = new QNotification("notification");

    public final com.ssuspot.sns.domain.model.common.QBaseTimeEntity _super = new com.ssuspot.sns.domain.model.common.QBaseTimeEntity(this);

    public final BooleanPath checked = createBoolean("checked");

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath link = createString("link");

    public final StringPath message = createString("message");

    public final StringPath title = createString("title");

    //inherited
    public final NumberPath<Long> updatedAt = _super.updatedAt;

    public QNotification(String variable) {
        super(Notification.class, forVariable(variable));
    }

    public QNotification(Path<Notification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotification(PathMetadata metadata) {
        super(Notification.class, metadata);
    }

}

