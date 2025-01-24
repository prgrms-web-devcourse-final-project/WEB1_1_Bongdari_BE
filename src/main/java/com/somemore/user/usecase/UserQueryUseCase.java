package com.somemore.user.usecase;

import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;

import java.util.UUID;

public interface UserQueryUseCase {

    User getById(UUID id);

    UserRole getRoleById(UUID userId);

    User getByAccountId(String accountId);

    UserCommonAttribute getCommonAttributeByUserId(UUID userId);

    boolean getIsCustomizedByUserId(UUID userId);

    boolean isDuplicateAccountId(String accountId);

    UserProfileDto getUserProfileByUserId(UUID userId);
}
