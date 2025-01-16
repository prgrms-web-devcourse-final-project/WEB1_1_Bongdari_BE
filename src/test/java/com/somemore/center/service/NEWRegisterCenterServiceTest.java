package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NEWRegisterCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private NEWRegisterCenterService registerCenterService;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Test
    @DisplayName("유저 아이디로 센터가 등록될 수 있다.")
    void registerByUserId() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        NEWCenter savedCenter = registerCenterService.register(userId);

        // then
        NEWCenter foundCenter = centerRepository.findByUserId(userId).orElseThrow();
        assertThat(savedCenter).isEqualTo(foundCenter);
    }

}