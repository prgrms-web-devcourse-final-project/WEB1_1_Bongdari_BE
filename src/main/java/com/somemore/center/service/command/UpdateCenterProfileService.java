package com.somemore.center.service.command;

import com.somemore.center.domain.Center;
import com.somemore.center.dto.request.CenterProfileUpdateRequestDto;
import com.somemore.center.repository.center.CenterRepository;
import com.somemore.center.usecase.command.UpdateCenterProfileUseCase;
import com.somemore.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateCenterProfileService implements UpdateCenterProfileUseCase {

    private final CenterRepository centerRepository;

    @Override
    public void updateCenterProfile(UUID centerId, CenterProfileUpdateRequestDto requestDto, String imgUrl) {
        Center center = centerRepository.findCenterById(centerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_CENTER));

        center.updateWith(requestDto, imgUrl);
    }
}
