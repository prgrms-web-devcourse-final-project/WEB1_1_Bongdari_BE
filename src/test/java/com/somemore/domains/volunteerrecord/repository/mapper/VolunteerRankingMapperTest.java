package com.somemore.domains.volunteerrecord.repository.mapper;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VolunteerRankingMapperTest {

    @DisplayName("유틸리티 클래스 인스턴스화 시도 시 예외 발생")
    @Test
    void utilityClassCannotBeInstantiated() throws Exception {
        // given
        Constructor<VolunteerRankingMapper> constructor = VolunteerRankingMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when & then
        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable cause = exception.getCause();
        assertThat(cause).isInstanceOf(UnsupportedOperationException.class);
        assertThat(cause.getMessage()).isEqualTo("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    @DisplayName("toTotalRankingResponse 메서드가 올바른 VolunteerTotalRankingResponseDto를 반환한다")
    @Test
    void toTotalRankingResponse_Success() {

        // given
        UUID id = UUID.randomUUID();
        Object[] result = {id, 100L, 1};

        // when
        VolunteerTotalRankingResponseDto dto = VolunteerRankingMapper.toTotalRankingResponse(result);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.volunteerId()).isEqualTo(id);
        assertThat(dto.ranking()).isEqualTo(1);
        assertThat(dto.totalHours()).isEqualTo(100L);
    }

    @DisplayName("toWeeklyRankingResponse 메서드가 올바른 VolunteerWeeklyRankingResponseDto를 반환한다")
    @Test
    void toWeeklyRankingResponse_Success() {

        // given
        UUID id = UUID.randomUUID();
        Object[] result = {id, 50L, 2};

        // when
        VolunteerWeeklyRankingResponseDto dto = VolunteerRankingMapper.toWeeklyRankingResponse(result);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.volunteerId()).isEqualTo(id);
        assertThat(dto.ranking()).isEqualTo(2);
        assertThat(dto.totalHours()).isEqualTo(50L);
    }

    @DisplayName("toMonthlyRankingResponse 메서드가 올바른 VolunteerMonthlyRankingResponseDto를 반환한다")
    @Test
    void toMonthlyRankingResponse_Success() {

        // given
        UUID id = UUID.randomUUID();
        Object[] result = {id, 200L, 3};

        // when
        VolunteerMonthlyRankingResponseDto dto = VolunteerRankingMapper.toMonthlyRankingResponse(result);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.volunteerId()).isEqualTo(id);
        assertThat(dto.ranking()).isEqualTo(3);
        assertThat(dto.totalHours()).isEqualTo(200L);
    }
}
