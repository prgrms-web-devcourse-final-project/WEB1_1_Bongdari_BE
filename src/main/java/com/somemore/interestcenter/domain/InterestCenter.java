package com.somemore.interestcenter.domain;

import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "interest_center")
public class InterestCenter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "volunteer_id", nullable = false)
    private UUID volunteerId;

    @Column(name = "center_id", nullable = false)
    private UUID centerId;

    @Builder
    private InterestCenter(UUID volunteerId, UUID centerId) {
        this.volunteerId = volunteerId;
        this.centerId = centerId;
    }

    public static InterestCenter create(UUID volunteerId, UUID centerId) {
        return InterestCenter.builder()
                .volunteerId(volunteerId)
                .centerId(centerId)
                .build();
    }
}
