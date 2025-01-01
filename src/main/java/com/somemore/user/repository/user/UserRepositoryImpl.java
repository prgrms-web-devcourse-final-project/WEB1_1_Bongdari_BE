package com.somemore.user.repository.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.user.domain.QUser;
import com.somemore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory queryFactory;
    private final UserJpaRepository userJpaRepository;

    private static final QUser user = QUser.user;

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(
                                user.id.eq(id),
                                isNotDeleted())
                        .fetchOne());
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    private static BooleanExpression isNotDeleted() {
        return user.deleted.eq(false);
    }
}
