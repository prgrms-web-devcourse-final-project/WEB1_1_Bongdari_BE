package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.RecruitStatus.RECRUITING;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.somemore.global.common.BaseEntity;
import com.somemore.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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

    @Embedded
    private VolunteerInfo volunteerInfo;

    @Enumerated(value = STRING)
    @Column(name = "recruit_status", nullable = false, length = 20)
    private RecruitStatus recruitStatus = RECRUITING;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Builder
    public RecruitBoard(UUID centerId, Long locationId, String title, String content,
        VolunteerInfo volunteerInfo, String imgUrl) {
        this.centerId = centerId;
        this.locationId = locationId;
        this.title = title;
        this.content = content;
        this.volunteerInfo = volunteerInfo;
        this.imgUrl = imgUrl;
    }

    public LocalTime getVolunteerHours() {
        return volunteerInfo.calculateVolunteerTime();
    }

    public boolean isWriter(UUID centerId) {
        return this.centerId.equals(centerId);
    }

    public boolean isNotWriter(UUID centerId) {
        return !isWriter(centerId);
    }

    public void updateWith(RecruitBoardUpdateRequestDto dto, String imgUrl) {
        updateVolunteerInfo(dto);
        this.title = dto.title();
        this.content = dto.content();
        this.imgUrl = imgUrl;
    }

    public void updateWith(String region) {
        volunteerInfo.updateWith(region);
    }

    private void updateVolunteerInfo(RecruitBoardUpdateRequestDto dto) {
        volunteerInfo.updateWith(
            dto.recruitmentCount(),
            dto.volunteerType(),
            dto.volunteerStartDateTime(),
            dto.volunteerEndDateTime(),
            dto.admitted()
        );
    }
}
