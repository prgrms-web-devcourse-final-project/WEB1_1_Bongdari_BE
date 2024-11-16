package com.somemore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Volunteer_management")
public class VolunteerManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "apply_status", nullable = false, length = 20)
    private String applyStatus;

    @ColumnDefault("0")
    @Column(name = "attended", nullable = false)
    private Boolean attended = false;

}