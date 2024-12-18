package com.somemore.domains.volunteer.repository;

import com.somemore.domains.volunteer.domain.Gender;
import com.somemore.domains.volunteer.domain.VolunteerDetail;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VolunteerDetailRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerDetailRepository volunteerDetailRepository;

    @DisplayName("봉사자 ID로 봉사자 상세 정보를 조회한다.")
    @Test
    void findByVolunteerId() {
        // given
        UUID volunteerId = UUID.randomUUID();
        VolunteerDetail volunteerDetail = createVolunteerDetail(volunteerId);

        volunteerDetailRepository.save(volunteerDetail);

        // when
        Optional<VolunteerDetail> foundDetail = volunteerDetailRepository.findByVolunteerId(volunteerId);

        // then
        assertThat(foundDetail).isPresent();
        assertThat(foundDetail.get().getVolunteerId()).isEqualTo(volunteerId);
        assertThat(foundDetail.get().getName()).isEqualTo(volunteerDetail.getName());
    }

    @DisplayName("봉사자 상세 정보를 저장한다.")
    @Test
    void saveVolunteerDetail() {
        // given
        UUID volunteerId = UUID.randomUUID();
        VolunteerDetail volunteerDetail = createVolunteerDetail(volunteerId);

        // when
        VolunteerDetail savedDetail = volunteerDetailRepository.save(volunteerDetail);

        // then
        assertThat(savedDetail).isNotNull();
        assertThat(savedDetail.getVolunteerId()).isEqualTo(volunteerId);
        assertThat(savedDetail.getName()).isEqualTo(volunteerDetail.getName());
    }

    private VolunteerDetail createVolunteerDetail(UUID volunteerId) {
        return VolunteerDetail.builder()
                .volunteerId(volunteerId)
                .name("making")
                .email("making@example.com")
                .gender(Gender.MALE)
                .birthDate("1998-06-08")
                .contactNumber("010-1234-5678")
                .build();
    }
}
