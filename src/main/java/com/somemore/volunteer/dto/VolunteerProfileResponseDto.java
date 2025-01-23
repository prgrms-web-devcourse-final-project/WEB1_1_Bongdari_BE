package com.somemore.volunteer.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.volunteer.domain.NEWVolunteer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "봉사자 프로필 응답 DTO")
@Builder
public record VolunteerProfileResponseDto(

        @Schema(description = "봉사자 닉네임", example = "길동이")
        String nickname,

        @Schema(description = "봉사자 등급", example = "RED")
        String tier,

        @Schema(description = "봉사자 이미지 URL", example = "http://example.com/image.jpg")
        String imgUrl,

        @Schema(description = "봉사자 소개", example = "안녕하세요! 봉사 활동을 좋아합니다.")
        String introduce,

        @Schema(description = "총 봉사 시간", example = "120")
        Integer totalVolunteerHours,

        @Schema(description = "총 봉사 횟수", example = "20")
        Integer totalVolunteerCount,

        @Schema(description = "봉사자 상세 정보", implementation = Detail.class)
        Detail detail
) {

    public static VolunteerProfileResponseDto of(
            NEWVolunteer volunteer,
            UserCommonAttribute commonAttribute
    ) {
        return VolunteerProfileResponseDto.builder()
                .nickname(volunteer.getNickname())
                .tier(volunteer.getTier().name())
                .imgUrl(commonAttribute.getImgUrl())
                .introduce(commonAttribute.getIntroduce())
                .totalVolunteerHours(volunteer.getTotalVolunteerHours())
                .totalVolunteerCount(volunteer.getTotalVolunteerCount())
                .detail(Detail.of(volunteer, commonAttribute))
                .build();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(description = "봉사자 상세 프로필")
    @Builder
    public record Detail(
            @Schema(description = "이름", example = "홍길동")
            String name,

            @Schema(description = "성별", example = "MALE")
            String gender,

            @Schema(description = "연락처", example = "010-1234-5678")
            String contactNumber
    ) {
        public static Detail of(
                NEWVolunteer volunteer,
                UserCommonAttribute commonAttribute
        ) {
            return Detail.builder()
                    .name(commonAttribute.getName())
                    .gender(volunteer.getGender().name())
                    .contactNumber(commonAttribute.getContactNumber())
                    .build();
        }
    }
}
