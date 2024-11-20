package com.somemore.recruitboard.dto.command;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record RecruitBoardCreateCommandRequestDto(
    Long centerId,
    Long locationId,
    String imgUrl,
    String title,
    String content,
    LocalDateTime volunteerDate,
    VolunteerType volunteerType,
    Integer volunteerHours,
    Boolean admitted
    ) {

    public RecruitBoard toEntity() {
        return RecruitBoard.builder()
            .locationId(locationId)
            .centerId(centerId)
            .title(title)
            .content(content)
            .imgUrl(imgUrl)
            .volunteerDate(volunteerDate)
            .volunteerType(volunteerType)
            .admitted(admitted)
            .build();
    }

}
