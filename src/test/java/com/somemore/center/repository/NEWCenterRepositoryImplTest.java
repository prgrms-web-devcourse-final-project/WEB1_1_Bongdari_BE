package com.somemore.center.repository;

import com.somemore.center.domain.NEWCenter;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
