package com.somemore.volunteerapply.domain;

import com.somemore.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
    public VolunteerApply(UUID volunteerId, Long recruitBoardId, ApplyStatus status, Boolean attended) {
        this.volunteerId = volunteerId;
        this.recruitBoardId = recruitBoardId;
        this.status = status;
        this.attended = attended;
    }
}

// TODO 상태 업데이트 메서드들을 만들고 빌더에서 status를 변경 불가하도록
