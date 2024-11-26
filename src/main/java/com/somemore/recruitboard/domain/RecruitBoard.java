package com.somemore.recruitboard.domain;

import static com.somemore.recruitboard.domain.RecruitStatus.CLOSED;
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

    @Embedded
    private RecruitmentInfo recruitmentInfo;

    @Enumerated(value = STRING)
    @Column(name = "recruit_status", nullable = false, length = 20)
    private RecruitStatus recruitStatus = RECRUITING;

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

    public LocalTime getVolunteerHours() {
        return recruitmentInfo.calculateVolunteerTime();
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

    private void updateRecruitmentInfo(RecruitBoardUpdateRequestDto dto) {
        recruitmentInfo.updateWith(
            dto.recruitmentCount(),
            dto.volunteerType(),
            dto.volunteerStartDateTime(),
            dto.volunteerEndDateTime(),
            dto.admitted()
        );
    }

    private void validateStatusChange(RecruitStatus newStatus) {
        if (newStatus != RECRUITING && newStatus != CLOSED) {
            throw new IllegalArgumentException("상태는 '모집중' 또는 '마감'으로만 변경할 수 있습니다.");
        }
    }

    private void validateChangeDeadline(LocalDateTime currentDateTime) {
        LocalDateTime volunteerStartDateTime = recruitmentInfo.getVolunteerStartDateTime();
        LocalDateTime deadline = volunteerStartDateTime.toLocalDate().atStartOfDay();

        if (!currentDateTime.isBefore(deadline)) {
            throw new IllegalStateException("봉사 시작 일시 자정 전까지만 상태를 변경할 수 있습니다.");
        }
    }

}
