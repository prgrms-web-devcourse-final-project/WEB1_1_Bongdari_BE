package com.somemore.volunteer.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "봉사자 응답 DTO")
public record VolunteerResponseDto(
        @Schema(description = "봉사자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        String volunteerId,

        @Schema(description = "봉사자 닉네임", example = "길동이")
        String nickname,

        @Schema(description = "봉사자 이미지 URL", example = "http://example.com/image.jpg")
        String imgUrl,

        @Schema(description = "봉사자 소개", example = "안녕하세요! 봉사 활동을 좋아합니다.")
        String introduce,

        @Schema(description = "봉사자 등급", example = "RED")
        String tier,

        @Schema(description = "총 봉사 시간", example = "120")
        Integer totalVolunteerHours,

        @Schema(description = "총 봉사 횟수", example = "20")
        Integer totalVolunteerCount,

        @Schema(description = "봉사자 상세 정보", implementation = VolunteerDetailResponseDto.class)
        VolunteerDetailResponseDto volunteerDetailResponseDto
) {

    public static VolunteerResponseDto from(
            Volunteer volunteer,
            VolunteerDetail volunteerDetail
    ) {
        return new VolunteerResponseDto(
                volunteer.getId().toString(),
                volunteer.getNickname(),
                volunteer.getImgUrl(),
                volunteer.getIntroduce(),
                volunteer.getTier().name(),
                volunteer.getTotalVolunteerHours(),
                volunteer.getTotalVolunteerCount(),
                VolunteerDetailResponseDto.from(volunteerDetail)
        );
    }

    public static VolunteerResponseDto from(
            Volunteer volunteer
    ) {
        return new VolunteerResponseDto(
                volunteer.getId().toString(),
                volunteer.getNickname(),
                volunteer.getImgUrl(),
                volunteer.getIntroduce(),
                volunteer.getTier().name(),
                volunteer.getTotalVolunteerHours(),
                volunteer.getTotalVolunteerCount(),
                null
        );
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(description = "봉사자 상세 응답 DTO")
    private record VolunteerDetailResponseDto(
            @Schema(description = "이름", example = "홍길동")
            String name,

            @Schema(description = "이메일", example = "honggildong@example.com")
            String email,

            @Schema(description = "성별", example = "MALE")
            String gender,

            @Schema(description = "생년월일", example = "1990-01-01")
            String birthDate,

            @Schema(description = "연락처", example = "010-1234-5678")
            String contactNumber
    ) {
        public static VolunteerDetailResponseDto from(
                VolunteerDetail volunteerDetail
        ) {
            return new VolunteerDetailResponseDto(
                    volunteerDetail.getName(),
                    volunteerDetail.getEmail(),
                    volunteerDetail.getGender().name(),
                    volunteerDetail.getBirthDate(),
                    volunteerDetail.getContactNumber()
            );
        }
    }
}
