package com.somemore.domains.volunteerrecord.domain;

import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "volunteer_record")
public class VolunteerRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "volunteer_id", nullable = false)
    private UUID volunteerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "volunteer_date", nullable = false)
    private LocalDate volunteerDate;

    @Column(name = "volunteer_hours", nullable = false)
    private int volunteerHours;

    @Builder
    private VolunteerRecord(UUID volunteerId, String title, LocalDate volunteerDate, int volunteerHours) {
        this.volunteerId = volunteerId;
        this.title = title;
        this.volunteerDate = volunteerDate;
        this.volunteerHours = volunteerHours;
    }

    public static VolunteerRecord create(UUID volunteerId, String title, LocalDate volunteerDate, int volunteerHours) {
        return VolunteerRecord.builder()
                .volunteerId(volunteerId)
                .title(title)
                .volunteerDate(volunteerDate)
                .volunteerHours(volunteerHours)
                .build();
    }

}
