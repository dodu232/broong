package org.example.broong.domain.reviews.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviews is a Querydsl query type for Reviews
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviews extends EntityPathBase<Reviews> {

    private static final long serialVersionUID = 430001462L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviews reviews = new QReviews("reviews");

    public final org.example.broong.domain.common.QBaseEntity _super = new org.example.broong.domain.common.QBaseEntity(this);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final org.example.broong.domain.order.entity.QOrder orderId;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final org.example.broong.domain.store.entity.QStore storeId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final org.example.broong.domain.user.entity.QUser userId;

    public QReviews(String variable) {
        this(Reviews.class, forVariable(variable), INITS);
    }

    public QReviews(Path<? extends Reviews> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviews(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviews(PathMetadata metadata, PathInits inits) {
        this(Reviews.class, metadata, inits);
    }

    public QReviews(Class<? extends Reviews> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderId = inits.isInitialized("orderId") ? new org.example.broong.domain.order.entity.QOrder(forProperty("orderId"), inits.get("orderId")) : null;
        this.storeId = inits.isInitialized("storeId") ? new org.example.broong.domain.store.entity.QStore(forProperty("storeId"), inits.get("storeId")) : null;
        this.userId = inits.isInitialized("userId") ? new org.example.broong.domain.user.entity.QUser(forProperty("userId")) : null;
    }

}

