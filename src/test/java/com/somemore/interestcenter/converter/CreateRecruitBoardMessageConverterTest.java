package com.somemore.interestcenter.converter;

import com.somemore.IntegrationTestSupport;
import com.somemore.global.common.event.DomainEventSubType;
import com.somemore.global.common.event.ServerEventType;
import com.somemore.interestcenter.event.converter.CreateRecruitBoardMessageConverter;
import com.somemore.recruitboard.event.CreateRecruitBoardEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateRecruitBoardMessageConverterTest extends IntegrationTestSupport {

    @Autowired
    private CreateRecruitBoardMessageConverter createRecruitBoardMessageConverter;

    @Test
    @DisplayName("CREATE_RECRUIT_BOARD 메시지를 변환하면 CreateRecruitBoardEvent 객체를 반환한다")
    void testVolunteerReviewRequestConversion() {
        // given
        String message = """
                {
                    "type": "DOMAIN_EVENT",
                    "subType": "CREATE_RECRUIT_BOARD",
                    "centerId": "123e4567-e89b-12d3-a456-426614174001",
                    "recruitBoardId": 456,
                    "createdAt": "2024-12-05T10:00:00"
                }
                """;

        // when
        CreateRecruitBoardEvent event = createRecruitBoardMessageConverter.from(message);

        // then
        assertThat(event.getCenterId()).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        assertThat(event.getRecruitBoardId()).isEqualTo(456);
        assertThat(event.getType()).isEqualTo(ServerEventType.DOMAIN_EVENT);
        assertThat(event.getSubType()).isEqualTo(DomainEventSubType.CREATE_RECRUIT_BOARD);
    }

    @Test
    @DisplayName("잘못된 메시지를 변환하려 하면 IllegalStateException이 발생한다")
    void testMessageConversion_Failure() {
        // given
        String invalidMessage = """
                {
                    "type": "DOMAIN_EVENT",
                    "subType": "INVALID_TYPE",
                    "centerId": "123e4567-e89b-12d3-a456-426614174001",
                    "recruitBoardId": 456,
                    "createdAt": "2024-12-05T10:00:00"
                }
                """;

        // when & then
        assertThatThrownBy(() -> createRecruitBoardMessageConverter.from(invalidMessage))
                .isInstanceOf(IllegalStateException.class);
    }

}
