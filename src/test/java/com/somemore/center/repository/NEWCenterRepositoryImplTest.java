package com.somemore.center.repository;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.record.CenterProfileDto;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NEWCenterRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private NEWCenterRepositoryImpl centerRepository;

    @DisplayName("유저 아이디로 기관을 등록할 수 있다.")
    @Test
    void saveVolunteerByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);

        // when
        centerRepository.save(center);

        // then
        NEWCenter centerByUserId = centerRepository.findByUserId(userId).orElseThrow();
        NEWCenter centerById = centerRepository.findById(center.getId()).orElseThrow();

        assertThat(center)
                .isEqualTo(centerByUserId)
                .isEqualTo(centerById);

    }

    @DisplayName("기관 아이디로 기관 프로필 정보를 가져올 수 있다.")
    @Test
    void findCenterProfileByUserId() {

        // given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        // when
        Optional<CenterProfileDto> result = centerRepository.findCenterProfileById(center.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get())
                .extracting("id", "userId")
                .containsExactly(center.getId(), userId);
    }

    @DisplayName("존재하지 않는 유저 아이디로 기관 정보 조회시 빈 값을 반환한다.")
    @Test
    void findCenterProfileByUserId_NoResult() {

        // given
        UUID nonExistentCenterId = UUID.randomUUID();

        // when
        Optional<CenterProfileDto> result = centerRepository.findCenterProfileById(nonExistentCenterId);

        // then
        assertThat(result).isEmpty();
    }
}
