package com.somemore.domains.recruitboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static java.time.temporal.ChronoUnit.MINUTES;
import static lombok.AccessLevel.PROTECTED;

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

    @Column(name = "volunteer_hours", nullable = false)
    private Integer volunteerHours;

    @Enumerated(value = STRING)
    @Column(name = "volunteer_category", nullable = false, length = 30)
    private VolunteerCategory volunteerCategory;

    @Column(name = "admitted", nullable = false)
    private Boolean admitted;

    @Builder
    public RecruitmentInfo(String region, Integer recruitmentCount, LocalDateTime volunteerStartDateTime, LocalDateTime volunteerEndDateTime,
                           Integer volunteerHours, VolunteerCategory volunteerCategory, Boolean admitted) {

        this.region = region;
        this.recruitmentCount = recruitmentCount;
        this.volunteerStartDateTime = volunteerStartDateTime.truncatedTo(MINUTES);
        this.volunteerEndDateTime = volunteerEndDateTime.truncatedTo(MINUTES);
        this.volunteerHours = volunteerHours;
        this.volunteerCategory = volunteerCategory;
        this.admitted = admitted;
    }

    public void updateWith(String region, Integer recruitmentCount, VolunteerCategory volunteerCategory,
                           LocalDateTime volunteerStartDateTime, LocalDateTime volunteerEndDateTime,
                           Integer volunteerHours, Boolean admitted) {
        this.region = region;
        this.recruitmentCount = recruitmentCount;
        this.volunteerCategory = volunteerCategory;
        this.volunteerStartDateTime = volunteerStartDateTime.truncatedTo(MINUTES);
        this.volunteerEndDateTime = volunteerEndDateTime.truncatedTo(MINUTES);
        this.volunteerHours = volunteerHours;
        this.admitted = admitted;
    }

    public void updateWith(String region) {
        this.region = region;
    }

}
