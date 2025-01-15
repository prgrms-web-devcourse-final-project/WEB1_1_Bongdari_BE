package com.somemore.volunteer.domain;

import com.somemore.domains.volunteer.domain.Gender;
import com.somemore.domains.volunteer.domain.Tier;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "new_volunteer") // TODO suffix 삭제
public class NEWVolunteer extends BaseEntity {

    public static final String DEFAULT_NAME = "봉사자";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, length = 20)
    private Tier tier;

    @Builder
    private NEWVolunteer(
            UUID userId,
            String name,
            Gender gender,
            Tier tier
    ) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.tier = tier;
    }

    public static NEWVolunteer createDefault(UUID userId) {
        return NEWVolunteer.builder()
                .userId(userId)
                .name(DEFAULT_NAME + userId.toString().substring(0, 8))
                .gender(Gender.getDefault())
                .tier(Tier.getDefault())
                .build();
    }
}
