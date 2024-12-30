package com.somemore.user.repository.user;

import com.somemore.user.domain.User;

public interface UserRepository {

    User save(User user);
}
