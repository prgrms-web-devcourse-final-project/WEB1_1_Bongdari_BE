package com.somemore.recruitboard.domain;

import static jakarta.persistence.EnumType.STRING;
import static java.time.temporal.ChronoUnit.MINUTES;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class RecruitmentInfo {

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "recruitment_count", nullable = false)
    private Integer recruitmentCount;

    @Column(name = "volunteer_start_date_time", nullable = false)
    private LocalDateTime volunteerStartDateTime;

    @Column(name = "volunteer_end_date_time", nullable = false)
    private LocalDateTime volunteerEndDateTime;

    @Enumerated(value = STRING)
    @Column(name = "volunteer_type", nullable = false, length = 30)
    private VolunteerType volunteerType;

    @Column(name = "admitted", nullable = false)
    private Boolean admitted;

    @Builder
    public RecruitmentInfo(String region, Integer recruitmentCount,
        LocalDateTime volunteerStartDateTime, LocalDateTime volunteerEndDateTime,
        VolunteerType volunteerType, Boolean admitted) {

        validateVolunteerDateTime(volunteerStartDateTime, volunteerEndDateTime);

        this.region = region;
        this.recruitmentCount = recruitmentCount;
        this.volunteerStartDateTime = volunteerStartDateTime.truncatedTo(MINUTES);
        this.volunteerEndDateTime = volunteerEndDateTime.truncatedTo(MINUTES);
        this.volunteerType = volunteerType;
        this.admitted = admitted;
    }

    public LocalTime calculateVolunteerTime() {
        Duration duration = Duration.between(volunteerStartDateTime, volunteerEndDateTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return LocalTime.of((int) hours, (int) minutes);
    }

    public void updateWith(Integer recruitmentCount, VolunteerType volunteerType,
        LocalDateTime volunteerStartDateTime, LocalDateTime volunteerEndDateTime,
        Boolean admitted) {

        validateVolunteerDateTime(volunteerStartDateTime, volunteerEndDateTime);

        this.recruitmentCount = recruitmentCount;
        this.volunteerType = volunteerType;
        this.volunteerStartDateTime = volunteerStartDateTime.truncatedTo(MINUTES);
        this.volunteerEndDateTime = volunteerEndDateTime.truncatedTo(MINUTES);
        this.admitted = admitted;
    }

    public void updateWith(String region) {
        this.region = region;
    }

    private void validateVolunteerDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isEqual(startDateTime) || endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 이후여야 합니다.");
        }
    }
}
