package com.somemore.user.repository.usersetting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.user.domain.QUserSetting;
import com.somemore.user.domain.UserSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserSettingRepositoryImpl implements UserSettingRepository {

    private final JPAQueryFactory queryFactory;
    private final UserSettingJpaRepository userSettingJpaRepository;

    private static final QUserSetting userSetting = QUserSetting.userSetting;

    @Override
    public Optional<UserSetting> findByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(userSetting)
                        .where(
                                userSetting.userId.eq(userId),
                                userSetting.deleted.eq(false)
                        )
                        .fetchOne()
        );
    }

    @Override
    public UserSetting save(UserSetting userSetting) {
        return userSettingJpaRepository.save(userSetting);
    }
}
