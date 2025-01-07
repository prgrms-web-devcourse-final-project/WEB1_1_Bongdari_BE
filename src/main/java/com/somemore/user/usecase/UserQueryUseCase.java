package com.somemore.user.usecase;

import com.somemore.user.domain.User;
import com.somemore.user.domain.UserCommonAttribute;

import java.util.UUID;

public interface UserQueryUseCase {

    User getById(UUID id);
    User getByAccountId(String accountId);
    UserCommonAttribute getCommonAttributeByUserId(UUID userId);
    boolean getIsCustomizedByUserId(UUID userId);
}
