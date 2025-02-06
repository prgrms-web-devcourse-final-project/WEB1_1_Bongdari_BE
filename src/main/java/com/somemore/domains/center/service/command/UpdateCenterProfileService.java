package com.somemore.domains.center.service.command;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_CENTER;

import com.somemore.center.usecase.UpdateCenterProfileUseCase;
import com.somemore.domains.center.domain.Center;
import com.somemore.domains.center.dto.request.CenterProfileUpdateRequestDto;
import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateCenterProfileService implements UpdateCenterProfileUseCase {

    private final CenterRepository centerRepository;

    @Override
    public void updateCenterProfile(UUID centerId, CenterProfileUpdateRequestDto requestDto,
            String imgUrl) {
        Center center = centerRepository.findCenterById(centerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_CENTER));

        center.updateWith(requestDto, imgUrl);
    }
}
