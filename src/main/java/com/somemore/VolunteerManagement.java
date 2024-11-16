package com.somemore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "Volunteer_management")
public class VolunteerManagement {
    @EmbeddedId
    private String id;

    @Column(name = "apply_status", nullable = false, length = 20)
    private String applyStatus;

    @ColumnDefault("0")
    @Column(name = "attended", nullable = false)
    private Boolean attended = false;

}