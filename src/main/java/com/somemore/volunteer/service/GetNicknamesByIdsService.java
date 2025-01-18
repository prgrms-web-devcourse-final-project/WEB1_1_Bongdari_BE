package com.somemore.volunteer.service;

import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.usecase.GetNicknamesByIdsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetNicknamesByIdsService implements GetNicknamesByIdsUseCase {

    private final NEWVolunteerRepository volunteerRepository;

    @Override
    public List<String> getNicknamesByIds(List<UUID> ids) {

        return volunteerRepository.findNicknamesByIds(ids);
    }
}
