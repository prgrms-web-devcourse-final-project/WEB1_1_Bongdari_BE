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
@Table(name = "new_volunteer") // TODO prefix 삭제
public class NEWVolunteer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, length = 20)
    private Tier tier;

    @Column(name = "total_volunteer_hours", nullable = false)
    private int totalVolunteerHours;

    @Column(name = "total_volunteer_count", nullable = false)
    private int totalVolunteerCount;

    @Builder
    private NEWVolunteer(
            UUID userId,
            String nickname,
            Gender gender,
            Tier tier,
            int totalVolunteerHours,
            int totalVolunteerCount
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.gender = gender;
        this.tier = tier;
        this.totalVolunteerHours = totalVolunteerHours;
        this.totalVolunteerCount = totalVolunteerCount;
    }

    public static NEWVolunteer createDefault(UUID userId) {
        return NEWVolunteer.builder()
                .userId(userId)
                .nickname(userId.toString().substring(0, 8))
                .gender(Gender.getDefault())
                .tier(Tier.getDefault())
                .totalVolunteerHours(0)
                .totalVolunteerCount(0)
                .build();
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void update(Gender gender) {
        this.gender = gender;
    }

    public void updateVolunteerStats(int hours, int count) {
        this.totalVolunteerHours += hours;
        this.totalVolunteerCount += count;
    }

}
