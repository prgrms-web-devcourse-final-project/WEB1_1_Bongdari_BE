package com.somemore.user.repository.usercommonattribute;

import com.somemore.user.domain.UserCommonAttribute;

import java.util.Optional;
import java.util.UUID;

public interface UserCommonAttributeRepository {

    Optional<UserCommonAttribute> findByUserId(UUID userId);
    UserCommonAttribute save(UserCommonAttribute userCommonAttribute);
    Optional<Boolean> findIsCustomizedByUserId(UUID userId);
}
