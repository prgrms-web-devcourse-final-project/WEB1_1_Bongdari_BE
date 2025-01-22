package com.somemore.volunteer.service;

import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class RegisterNEWVolunteerServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterNEWVolunteerService registerVolunteerService;

    @Autowired
    private NEWVolunteerRepository volunteerRepository;

    @Test
    @DisplayName("유저 아이디로 봉사자가 등록될 수 있다.")
    void registerByUserId() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        registerVolunteerService.register(userId);

        // then
        Optional<NEWVolunteer> foundVolunteer = volunteerRepository.findByUserId(userId);
        assertThat(foundVolunteer).isNotEmpty();
    }
}
