package com.somemore.domains.volunteer.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.domains.volunteer.repository.mapper.VolunteerOverviewForRankingByHours;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record VolunteerRankingResponseDto(
        @Schema(description = "랭킹에 포함된 봉사자 리스트")
        List<VolunteerOverview> rankings
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    public record VolunteerOverview(
            @Schema(description = "봉사자 ID", example = "uuid-uuid-uuid-uuid")
            String volunteerId,

            @Schema(description = "봉사자 닉네임", example = "길동이")
            String nickname,

            @Schema(description = "봉사자 이미지 URL", example = "http://example.com/image.jpg")
            String imgUrl,

            @Schema(description = "봉사자 소개글", example = "안녕하세요! 저는 자원봉사자 홍길동입니다.")
            String introduce,

            @Schema(description = "봉사자 티어", example = "red")
            String tier,

            @Schema(description = "봉사자의 총 봉사 시간", example = "120")
            Integer totalVolunteerHours
    ) {
        private static VolunteerOverview from(VolunteerOverviewForRankingByHours source) {
            return VolunteerOverview.builder()
                    .volunteerId(source.volunteerId().toString())
                    .nickname(source.nickname())
                    .imgUrl(source.imgUrl())
                    .introduce(source.introduce())
                    .tier(source.tier().name())
                    .totalVolunteerHours(source.totalVolunteerHours())
                    .build();
        }
    }

    public static VolunteerRankingResponseDto from(List<VolunteerOverviewForRankingByHours> sources) {
        return VolunteerRankingResponseDto.builder()
                .rankings(sources.stream()
                                .map(VolunteerOverview::from)
                                .toList())
                .build();
    }
}
