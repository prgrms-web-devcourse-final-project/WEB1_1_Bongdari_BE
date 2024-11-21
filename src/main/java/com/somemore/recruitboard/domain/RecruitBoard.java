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

    @Column(name = "volunteer_date", nullable = false)
    private LocalDateTime volunteerDate;

    @Enumerated(value = STRING)
    @Column(name = "volunteer_type", nullable = false, length = 30)
    private VolunteerType volunteerType;

    @Column(name = "volunteer_hours", nullable = false)
    private LocalTime volunteerHours;

    @Column(name = "admitted", nullable = false)
    private Boolean admitted;

    @Builder
    public RecruitBoard(UUID centerId, Long locationId, String title, String content, String region,
        Integer recruitmentCount, String imgUrl, LocalDateTime volunteerDate,
        VolunteerType volunteerType, LocalTime volunteerHours, Boolean admitted) {
        this.centerId = centerId;
        this.locationId = locationId;
        this.title = title;
        this.content = content;
        this.region = region;
        this.recruitmentCount = recruitmentCount;
        this.imgUrl = imgUrl;
        this.volunteerDate = volunteerDate;
        this.volunteerType = volunteerType;
        this.volunteerHours = volunteerHours;
        this.admitted = admitted;
    }
}