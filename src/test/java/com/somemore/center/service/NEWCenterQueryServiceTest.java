package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NEWCenterQueryServiceTest extends IntegrationTestSupport{

    @Autowired
    private NEWCenterQueryService centerQueryService;

    @Autowired
    private NEWCenterRepository centerRepository;

    UUID userId;
    NEWCenter center;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        center = NEWCenter.createDefault(userId);
        centerRepository.save(center);
    }

    @Test
    @DisplayName("사용자 ID로 기관을 조회한다")
    void getByUserId() {
        NEWCenter foundCenter = centerQueryService.getByUserId(userId);

        assertThat(foundCenter).isEqualTo(center);
    }

    @Test
    @DisplayName("사용자 ID로 기관 ID를 조회한다")
    void getIdByUserId() {
        UUID foundCenterId = centerQueryService.getIdByUserId(userId);

        assertThat(foundCenterId).isEqualTo(center.getId());
    }
}
