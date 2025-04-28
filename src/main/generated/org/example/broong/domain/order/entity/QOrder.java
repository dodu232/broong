package org.example.broong.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 1598463862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final org.example.broong.domain.menu.entity.QMenu menu;

    public final EnumPath<org.example.broong.domain.order.enums.OrderStatus> orderStatus = createEnum("orderStatus", org.example.broong.domain.order.enums.OrderStatus.class);

    public final org.example.broong.domain.store.entity.QStore store;

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final org.example.broong.domain.user.entity.QUser user;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.menu = inits.isInitialized("menu") ? new org.example.broong.domain.menu.entity.QMenu(forProperty("menu"), inits.get("menu")) : null;
        this.store = inits.isInitialized("store") ? new org.example.broong.domain.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
        this.user = inits.isInitialized("user") ? new org.example.broong.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

