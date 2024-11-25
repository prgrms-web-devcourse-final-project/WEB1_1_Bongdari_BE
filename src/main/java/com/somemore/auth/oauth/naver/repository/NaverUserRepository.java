package com.somemore.auth.oauth.naver.repository;

import com.somemore.auth.oauth.naver.domain.NaverUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverUserRepository extends JpaRepository<NaverUser, String> {
}
