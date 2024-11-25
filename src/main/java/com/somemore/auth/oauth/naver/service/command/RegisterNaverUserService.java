package com.somemore.auth.oauth.naver.service.command;

import com.somemore.auth.oauth.naver.domain.NaverUser;
import com.somemore.auth.oauth.naver.repository.NaverUserRepository;
import com.somemore.auth.oauth.naver.usecase.command.RegisterNaverUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RegisterNaverUserService implements RegisterNaverUserUseCase {

    private final NaverUserRepository naverUserRepository;

    @Override
    public void registerNaverUser(String oAuthId) {
        NaverUser naverUser = NaverUser.from(oAuthId);

        naverUserRepository.save(naverUser);
    }
}
