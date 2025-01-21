package com.somemore.domains.recruitboard.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Embedded
    private RecruitmentInfo recruitmentInfo;

    @Enumerated(value = STRING)
    @Column(name = "recruit_status", nullable = false, length = 20)
    private RecruitStatus recruitStatus;

    @Builder
    public RecruitBoard(UUID centerId, Long locationId, String title, String content,
            RecruitmentInfo recruitmentInfo, RecruitStatus status) {
        this.centerId = centerId;
        this.locationId = locationId;
        this.title = title;
        this.content = content;
        this.recruitmentInfo = recruitmentInfo;
        this.recruitStatus = status;
    }

    public boolean isWriter(UUID centerId) {
        return this.centerId.equals(centerId);
    }

    public void updateWith(RecruitBoardUpdateRequestDto dto) {
        updateRecruitmentInfo(dto);
        this.title = dto.title();
        this.content = dto.content();
    }

    public void updateWith(String region) {
        recruitmentInfo.updateWith(region);
    }

    public void updateRecruitStatus(RecruitStatus status) {
        this.recruitStatus = status;
    }

    public boolean isRecruiting() {
        return this.recruitStatus == RecruitStatus.RECRUITING;
    }

    public boolean isCompleted() {
        return this.recruitStatus == RecruitStatus.COMPLETED;
    }

    public boolean isUpdatable(LocalDateTime current) {
        LocalDateTime deadline = this.recruitmentInfo.getVolunteerStartDateTime().toLocalDate().atStartOfDay();
        return current.isBefore(deadline);
    }

    private void updateRecruitmentInfo(RecruitBoardUpdateRequestDto dto) {
        recruitmentInfo.updateWith(
                dto.region(),
                dto.recruitmentCount(),
                dto.volunteerCategory(),
                dto.volunteerStartDateTime(),
                dto.volunteerEndDateTime(),
                dto.volunteerHours(),
                dto.admitted()
        );
    }

}
