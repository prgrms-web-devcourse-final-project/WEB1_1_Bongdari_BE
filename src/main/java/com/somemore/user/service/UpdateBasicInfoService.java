package com.somemore.user.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.usecase.NEWCenterQueryUseCase;
import com.somemore.domains.volunteer.domain.Gender;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.dto.basicinfo.CenterBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.CommonBasicInfoRequestDto;
import com.somemore.user.dto.basicinfo.VolunteerBasicInfoRequestDto;
import com.somemore.user.usecase.UpdateBasicInfoUseCase;
import com.somemore.user.usecase.UserQueryUseCase;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UpdateBasicInfoService implements UpdateBasicInfoUseCase {

    private final UserQueryUseCase userQueryUseCase;
    private final NEWVolunteerQueryUseCase volunteerQueryUseCase;
    private final NEWCenterQueryUseCase centerQueryUseCase;

    @Override
    public void update(UUID userId, UUID roleId, VolunteerBasicInfoRequestDto volunteerBasicInfoRequestDto) {
        updateCommonAttribute(userId, volunteerBasicInfoRequestDto.commonBasicInfo());
        updateVolunteerAttribute(userId, volunteerBasicInfoRequestDto);
    }

    @Override
    public void update(UUID userId, UUID roleId, CenterBasicInfoRequestDto centerBasicInfoRequestDto) {
        updateCommonAttribute(userId, centerBasicInfoRequestDto.commonBasicInfo());
        updateCenterAttribute(userId, centerBasicInfoRequestDto);
    }

    private void updateCommonAttribute(UUID userId, CommonBasicInfoRequestDto commonBasicInfoRequestDto) {
        UserCommonAttribute commonAttribute = userQueryUseCase.getCommonAttributeByUserId(userId);
        commonAttribute.update(commonBasicInfoRequestDto);
    }

    private void updateVolunteerAttribute(UUID userId, VolunteerBasicInfoRequestDto volunteerBasicInfoRequestDto) {
        NEWVolunteer volunteer = volunteerQueryUseCase.getByUserId(userId);
        volunteer.update(volunteerBasicInfoRequestDto.nickname());
        volunteer.update(Gender.from(volunteerBasicInfoRequestDto.gender()));
    }

    private void updateCenterAttribute(UUID userId, CenterBasicInfoRequestDto centerBasicInfoRequestDto) {
        NEWCenter center = centerQueryUseCase.getByUserId(userId);
        center.update(centerBasicInfoRequestDto.homepageUrl());
    }
}
