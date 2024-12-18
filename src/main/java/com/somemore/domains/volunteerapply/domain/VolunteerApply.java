package com.somemore.domains.volunteerapply.domain;

import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "volunteer_apply")
public class VolunteerApply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "volunteer_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID volunteerId;

    @Column(name = "recruit_board_id", nullable = false)
    private Long recruitBoardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplyStatus status = ApplyStatus.WAITING;

    @Column(name = "attended", nullable = false)
    private Boolean attended = false;

    @Builder
    public VolunteerApply(UUID volunteerId, Long recruitBoardId, ApplyStatus status,
            Boolean attended) {
        this.volunteerId = volunteerId;
        this.recruitBoardId = recruitBoardId;
        this.status = status;
        this.attended = attended;
    }

    public void changeStatus(ApplyStatus status) {
        if (isVolunteerActivityCompleted()) {
            throw new IllegalStateException("이미 완료된 봉사활동에 대해서는 변경이 불가능합니다.");
        }
        this.status = status;
    }

    public void changeAttended(Boolean attended) {
        if (this.status != ApplyStatus.APPROVED) {
            throw new IllegalStateException("승인되지 않은 봉사 지원은 참석 여부를 변경할 수 없습니다.");
        }
        this.attended = attended;
    }

    public boolean isOwnApplication(UUID volunteerId) {
        return this.volunteerId.equals(volunteerId);
    }


    public boolean isVolunteerActivityCompleted() {
        return this.attended && this.status == ApplyStatus.APPROVED;
    }
}
