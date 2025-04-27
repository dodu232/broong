package org.example.broong.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 667791782L;

    public static final QUser user = new QUser("user");

    public final org.example.broong.domain.common.QBaseEntity _super = new org.example.broong.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<org.example.broong.domain.user.enums.LoginType> loginType = createEnum("loginType", org.example.broong.domain.user.enums.LoginType.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final EnumPath<org.example.broong.domain.user.enums.UserType> userType = createEnum("userType", org.example.broong.domain.user.enums.UserType.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

