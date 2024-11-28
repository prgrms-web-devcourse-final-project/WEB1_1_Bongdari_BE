package com.somemore.volunteer.dto.response;

import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.domain.VolunteerDetail;

public record VolunteerResponseDto(
        String volunteerId,
        String nickname,
        String imgUrl,
        String introduce,
        String tier,
        Integer totalVolunteerHours,
        Integer totalVolunteerCount,
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

    private record VolunteerDetailResponseDto(
            String name,
            String email,
            String gender,
            String birthDate,
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


