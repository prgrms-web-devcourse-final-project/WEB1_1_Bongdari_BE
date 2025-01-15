package com.somemore.domains.recruitboard.domain;

import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
    private RecruitStatus recruitStatus = RecruitStatus.RECRUITING;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Builder
    public RecruitBoard(UUID centerId, Long locationId, String title, String content,
                        RecruitmentInfo recruitmentInfo, String imgUrl) {
        this.centerId = centerId;
        this.locationId = locationId;
        this.title = title;
        this.content = content;
        this.recruitmentInfo = recruitmentInfo;
        this.imgUrl = imgUrl;
    }

    public boolean isWriter(UUID centerId) {
        return this.centerId.equals(centerId);
    }

    public void updateWith(RecruitBoardUpdateRequestDto dto, String imgUrl) {
        updateRecruitmentInfo(dto);
        this.title = dto.title();
        this.content = dto.content();
        this.imgUrl = imgUrl;
    }

    public void updateWith(String region) {
        recruitmentInfo.updateWith(region);
    }

    public void changeRecruitStatus(RecruitStatus newStatus, LocalDateTime currentDateTime) {
        validateStatusChange(newStatus);
        validateChangeDeadline(currentDateTime);

        this.recruitStatus = newStatus;
    }

    public boolean isRecruitOpen() {
        return this.recruitStatus == RecruitStatus.RECRUITING;
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

    public boolean isCompleted() {
        return this.recruitStatus == RecruitStatus.COMPLETED;
    }

    private void validateStatusChange(RecruitStatus newStatus) {
        if (newStatus.isChangeable()) {
            return;
        }
        throw new IllegalArgumentException("상태는 '모집중' 또는 '마감'으로만 변경할 수 있습니다.");
    }

    private void validateChangeDeadline(LocalDateTime currentDateTime) {
        LocalDateTime volunteerStartDateTime = recruitmentInfo.getVolunteerStartDateTime();
        LocalDateTime deadline = volunteerStartDateTime.toLocalDate().atStartOfDay();

        if (!currentDateTime.isBefore(deadline)) {
            throw new IllegalStateException("봉사 시작 일시 자정 전까지만 상태를 변경할 수 있습니다.");
        }
    }

}
