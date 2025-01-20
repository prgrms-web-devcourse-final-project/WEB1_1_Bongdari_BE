package com.somemore.volunteer.service;

import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.repository.record.VolunteerNickname;
import com.somemore.volunteer.usecase.GetVolunteerNicknamesByIdsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetVolunteerNicknamesByIdsService implements GetVolunteerNicknamesByIdsUseCase {

    private final NEWVolunteerRepository volunteerRepository;

    @Override
    public List<VolunteerNickname> getNicknamesByIds(List<UUID> ids) {

        return volunteerRepository.findNicknamesByIds(ids);
    }
}
