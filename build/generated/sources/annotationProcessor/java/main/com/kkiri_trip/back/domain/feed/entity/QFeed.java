package com.kkiri_trip.back.domain.feed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFeed is a Querydsl query type for Feed
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeed extends EntityPathBase<Feed> {

    private static final long serialVersionUID = -318106439L;

    public static final QFeed feed = new QFeed("feed");

    public final com.kkiri_trip.back.domain.common.entity.QBaseEntity _super = new com.kkiri_trip.back.domain.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFeed(String variable) {
        super(Feed.class, forVariable(variable));
    }

    public QFeed(Path<? extends Feed> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFeed(PathMetadata metadata) {
        super(Feed.class, metadata);
    }

}

