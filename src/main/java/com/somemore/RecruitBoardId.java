package com.somemore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RecruitBoardId implements java.io.Serializable {
    private static final long serialVersionUID = 7357479905936631832L;
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "center_id", nullable = false, length = 16)
    private String centerId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecruitBoardId entity = (RecruitBoardId) o;
        return Objects.equals(this.centerId, entity.centerId) &&
                Objects.equals(this.locationId, entity.locationId) &&
                Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerId, locationId, id);
    }

}