package com.somemore.global.auth.oauth.naver.service.query;

import com.somemore.global.auth.oauth.naver.repository.NaverUserRepository;
import com.somemore.global.auth.oauth.naver.usecase.query.CheckNaverUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckNaverUserService implements CheckNaverUserUseCase {

    private final NaverUserRepository naverUserRepository;

    @Override
    public boolean isNaverUserExists(String id) {
        return naverUserRepository.existsById(id);
    }
}
