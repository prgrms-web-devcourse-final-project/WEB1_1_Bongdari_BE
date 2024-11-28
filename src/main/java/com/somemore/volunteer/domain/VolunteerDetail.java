package com.somemore.volunteer.domain;

import com.somemore.global.common.BaseEntity;
import com.somemore.volunteer.dto.request.VolunteerRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "volunteer_detail")
public class VolunteerDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "volunteer_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID volunteerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @Column(name = "birth_date", nullable = false, length = 10)
    private String birthDate;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;


    public static VolunteerDetail of(VolunteerRegisterRequestDto dto, UUID volunteerId) {
        return VolunteerDetail.builder()
                .volunteerId(volunteerId)
                .name(dto.name())
                .email(dto.email())
                .gender(Gender.from(dto.gender()))
                .birthDate(String.format("%s-%s", dto.birthyear(), dto.birthday()))
                .contactNumber(dto.mobile())
                .build();
    }

    @Builder
    private VolunteerDetail(
            UUID volunteerId,
            String name,
            String email,
            Gender gender,
            String birthDate,
            String contactNumber
    ) {
        this.volunteerId = volunteerId;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.contactNumber = contactNumber;
    }
}
