package com.somemore.user.repository.usercommonattribute;

import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCommonAttributeRepository {

    Optional<UserCommonAttribute> findByUserId(UUID userId);

    UserCommonAttribute save(UserCommonAttribute userCommonAttribute);

    Optional<Boolean> findIsCustomizedByUserId(UUID userId);

    Optional<UserProfileDto> findUserProfileByUserId(UUID userId);
    List<UserCommonAttribute> findAllByUserIds(List<UUID> userIds);
}
