package com.somemore.user.repository.usercommonattribute;

import com.somemore.user.domain.UserCommonAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCommonAttributeRepositoryImpl implements UserCommonAttributeRepository {

    private final UserCommonAttributeJpaRepository userCommonAttributeJpaRepository;

    @Override
    public UserCommonAttribute save(UserCommonAttribute userCommonAttribute) {
        return userCommonAttributeJpaRepository.save(userCommonAttribute);
    }
}
