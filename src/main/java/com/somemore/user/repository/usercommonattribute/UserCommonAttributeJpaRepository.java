package com.somemore.user.repository.usercommonattribute;

import com.somemore.user.domain.UserCommonAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommonAttributeJpaRepository extends JpaRepository<UserCommonAttribute, Long> {
}
