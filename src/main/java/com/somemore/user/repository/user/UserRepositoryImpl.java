package com.somemore.user.repository.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    private static final com.somemore.user.domain.QUser user = com.somemore.user.domain.QUser.user;

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(isNotDeleted())
                        .fetchOne());
    }

    private static BooleanExpression isNotDeleted() {
        return user.deleted.eq(false);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
