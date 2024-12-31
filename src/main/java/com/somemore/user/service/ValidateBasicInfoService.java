package com.somemore.user.service;

import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.user.usecase.ValidateBasicInfoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidateBasicInfoService implements ValidateBasicInfoUseCase {

    private final UserQueryUseCase userQueryUseCase;

    @Override
    public boolean isBasicInfoComplete(UUID userId) {
        // 객체지향적: UserCommonAttribute 객체를 활용하여 데이터를 관리
        // 최신 데이터와는 조금 멀어질 수 있음
        UserCommonAttribute userCommonAttribute = userQueryUseCase.getCommonAttributeByUserId(userId);
        boolean isCustomized1 = userCommonAttribute.isCustomized();

        // 비용 절감: 특정 필드만 조회하여 데이터베이스 접근을 최소화
        // 최신 데이터에 더 가까움
        boolean isCustomized2 = userQueryUseCase.getIsCustomizedByUserId(userId);

        // TODO:
        // - 두 접근 방식 중 하나를 선택.

        return isCustomized1 && isCustomized2;
    }
}
