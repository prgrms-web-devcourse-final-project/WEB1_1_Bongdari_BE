package com.somemore.domains.volunteerrecord.controller;

import com.somemore.domains.volunteerrecord.dto.response.*;
import com.somemore.domains.volunteerrecord.usecase.GetVolunteerRankingUseCase;
import com.somemore.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VolunteerRankingApiControllerTest extends ControllerTestSupport {

    @MockBean
    private GetVolunteerRankingUseCase getVolunteerRankingUseCase;

    @DisplayName("봉사 시간 랭킹을 조회할 수 있다.")
    @Test
    void getVolunteerRanking_ShouldReturnVolunteerRankings() throws Exception {

        // given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();
        UUID id5 = UUID.randomUUID();
        UUID id6 = UUID.randomUUID();

        List<VolunteerTotalRankingResponseDto> totalRankings = List.of(
                new VolunteerTotalRankingResponseDto(id1, 150, 1, "봉사자1"),
                new VolunteerTotalRankingResponseDto(id2, 120, 2, "봉사자2")
        );

        List<VolunteerMonthlyRankingResponseDto> monthlyRankings = List.of(
                new VolunteerMonthlyRankingResponseDto(id3, 50, 1, "봉사자1"),
                new VolunteerMonthlyRankingResponseDto(id4, 40, 2, "봉사자2")
        );

        List<VolunteerWeeklyRankingResponseDto> weeklyRankings = List.of(
                new VolunteerWeeklyRankingResponseDto(id5, 15, 1, "봉사자1"),
                new VolunteerWeeklyRankingResponseDto(id6, 10, 2, "봉사자2")
        );

        VolunteerRankingResponseDto responseDto = VolunteerRankingResponseDto.of(totalRankings, monthlyRankings, weeklyRankings);

        given(getVolunteerRankingUseCase.getVolunteerRanking()).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/volunteerrecord/ranking")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("봉사 시간 랭킹 반환 성공"))
                .andExpect(jsonPath("$.data.volunteer_total_ranking_response[0].volunteerId").value(id1.toString()))
                .andExpect(jsonPath("$.data.volunteer_total_ranking_response[0].totalHours").value(150))
                .andExpect(jsonPath("$.data.volunteer_total_ranking_response[0].ranking").value(1))
                .andExpect(jsonPath("$.data.volunteer_monthly_response[0].volunteerId").value(id3.toString()))
                .andExpect(jsonPath("$.data.volunteer_monthly_response[0].totalHours").value(50))
                .andExpect(jsonPath("$.data.volunteer_monthly_response[0].ranking").value(1))
                .andExpect(jsonPath("$.data.volunteer_weekly_ranking_response[0].volunteerId").value(id5.toString()))
                .andExpect(jsonPath("$.data.volunteer_weekly_ranking_response[0].totalHours").value(15))
                .andExpect(jsonPath("$.data.volunteer_weekly_ranking_response[0].ranking").value(1));

    }


    @DisplayName("봉사 시간 랭킹이 없을 경우 빈 리스트를 반환한다.")
    @Test
    void getVolunteerRanking_ShouldReturnEmptyLists_WhenNoRankings() throws Exception {
        // given
        VolunteerRankingResponseDto responseDto = VolunteerRankingResponseDto.of(List.of(), List.of(), List.of());

        given(getVolunteerRankingUseCase.getVolunteerRanking()).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/volunteerrecord/ranking")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("봉사 시간 랭킹 반환 성공"))
                .andExpect(jsonPath("$.data.volunteer_total_ranking_response").isEmpty())
                .andExpect(jsonPath("$.data.volunteer_monthly_response").isEmpty())
                .andExpect(jsonPath("$.data.volunteer_weekly_ranking_response").isEmpty());
    }
}
