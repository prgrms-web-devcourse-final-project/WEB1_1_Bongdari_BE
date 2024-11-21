package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.somemore.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "recruit_board")
public class RecruitBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "recruitment_count", nullable = false)
    private Integer recruitmentCount;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Enumerated(value = STRING)
    @Column(name = "recruit_status", nullable = false, length = 20)
    private RecruitStatus recruitStatus = RECRUITING;

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
    public RecruitBoard(UUID centerId, Long locationId, String title, String content, String region,
        Integer recruitmentCount, String imgUrl, LocalDateTime volunteerStartDateTime,
        LocalDateTime volunteerEndDateTime, VolunteerType volunteerType, Boolean admitted) {

        validateVolunteerDateTime(volunteerStartDateTime, volunteerEndDateTime);

        this.centerId = centerId;
        this.locationId = locationId;
        this.title = title;
        this.content = content;
        this.region = region;
        this.recruitmentCount = recruitmentCount;
        this.imgUrl = imgUrl;
        this.volunteerStartDateTime = volunteerStartDateTime;
        this.volunteerEndDateTime = volunteerEndDateTime;
        this.volunteerType = volunteerType;
        this.admitted = admitted;
    }

    public LocalTime calculateVolunteerTime() {
        Duration duration = Duration.between(volunteerStartDateTime, volunteerEndDateTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return LocalTime.of((int) hours, (int) minutes);
    }

    private void validateVolunteerDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isEqual(startDateTime) || endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 이후여야 합니다.");
        }
    }
}