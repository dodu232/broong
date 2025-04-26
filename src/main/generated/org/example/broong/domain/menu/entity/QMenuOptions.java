package org.example.broong.domain.menu.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenuOptions is a Querydsl query type for MenuOptions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuOptions extends EntityPathBase<MenuOptions> {

    private static final long serialVersionUID = -223988752L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenuOptions menuOptions = new QMenuOptions("menuOptions");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMenu menu;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QMenuOptions(String variable) {
        this(MenuOptions.class, forVariable(variable), INITS);
    }

    public QMenuOptions(Path<? extends MenuOptions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMenuOptions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMenuOptions(PathMetadata metadata, PathInits inits) {
        this(MenuOptions.class, metadata, inits);
    }

    public QMenuOptions(Class<? extends MenuOptions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.menu = inits.isInitialized("menu") ? new QMenu(forProperty("menu"), inits.get("menu")) : null;
    }

}

