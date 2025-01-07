package com.somemore.user.repository.user;

import com.somemore.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByAccountId(String accountId);

    User save(User user);
}
