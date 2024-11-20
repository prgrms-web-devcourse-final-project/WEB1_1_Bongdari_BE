package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "Recruit_board")
public class RecruitBoard {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "center_id", nullable = false)
    private Long centerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(value = STRING)
    @Column(name = "recruit_status", nullable = false, length = 20)
    private RecruitStatus recruitStatus = RECRUITING;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "volunteer_date", nullable = false)
    private LocalDateTime volunteerDate;

    @Enumerated(value = STRING)
    @Column(name = "volunteer_type", nullable = false, length = 20)
    private VolunteerType volunteerType;

    @Column(name = "volunteer_hours", nullable = false)
    private int volunteerHours;

    @Column(name = "admitted", nullable = false)
    private Boolean admitted;

    @Builder
    public RecruitBoard(Long locationId, Long centerId, String title, String content, String imgUrl,
        LocalDateTime volunteerDate, VolunteerType volunteerType, int volunteerHours,
        Boolean admitted) {
        this.locationId = locationId;
        this.centerId = centerId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.volunteerDate = volunteerDate;
        this.volunteerType = volunteerType;
        this.volunteerHours = volunteerHours;
        this.admitted = admitted;
    }
}