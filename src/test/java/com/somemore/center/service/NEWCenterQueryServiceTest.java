package com.somemore.center.service;

import com.somemore.center.domain.NEWCenter;
import com.somemore.center.dto.response.CenterProfileResponseDto;
import com.somemore.center.repository.NEWCenterRepository;
import com.somemore.domains.center.domain.PreferItem;
import com.somemore.domains.center.dto.response.PreferItemResponseDto;
import com.somemore.domains.center.repository.preferitem.PreferItemJpaRepository;
import com.somemore.domains.center.usecase.query.PreferItemQueryUseCase;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.user.domain.UserRole.CENTER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class NEWCenterQueryServiceTest extends IntegrationTestSupport{

    @Autowired
    private NEWCenterQueryService centerQueryService;

    @Autowired
    private NEWCenterRepository centerRepository;

    @Autowired
    private PreferItemQueryUseCase preferItemQueryUseCase;

    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;

    @Autowired
    private PreferItemJpaRepository preferItemRepository;

    @Test
    @DisplayName("사용자 ID로 기관을 조회한다")
    void getByUserId() {

        //given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        //when
        NEWCenter foundCenter = centerQueryService.getByUserId(userId);

        //then
        assertThat(foundCenter).isEqualTo(center);
    }

    @Test
    @DisplayName("사용자 ID로 기관 ID를 조회한다")
    void getIdByUserId() {

        //given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        // when
        UUID foundCenterId = centerQueryService.getIdByUserId(userId);

        //then
        assertThat(foundCenterId).isEqualTo(center.getId());
    }

    @DisplayName("기관의 유저 ID로 기관 정보를 조회할 수 있다.")
    @Test
    void getCenterProfileByUserId() {

        //given
        UUID userId = UUID.randomUUID();
        NEWCenter center = NEWCenter.createDefault(userId);
        centerRepository.save(center);

        UserCommonAttribute userCommonAttribute = UserCommonAttribute.createDefault(userId, CENTER);
        userCommonAttributeRepository.save(userCommonAttribute);

        PreferItem preferItem = PreferItem.create(center.getId(), "어린이 동화책");
        PreferItem preferItem1 = PreferItem.create(center.getId(), "간식");
        preferItemRepository.saveAll(List.of(preferItem, preferItem1));

        List<PreferItemResponseDto> preferItems = preferItemQueryUseCase.getPreferItemDtosByCenterId(center.getId());

        //when
        CenterProfileResponseDto result = centerQueryService.getCenterProfileById(center.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result)
                .extracting("id", "userId", "homepageUrl", "name", "contactNumber", "imgUrl", "introduce", "preferItems")
                .containsExactly(
                        center.getId(),
                        userId,
                        center.getHomepageUrl(),
                        userCommonAttribute.getName(),
                        userCommonAttribute.getContactNumber(),
                        userCommonAttribute.getImgUrl(),
                        userCommonAttribute.getIntroduce(),
                        preferItems
                );
    }
}
