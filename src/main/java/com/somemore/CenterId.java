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
public class CenterId implements java.io.Serializable {
    private static final long serialVersionUID = 1285878262246918904L;
    @Column(name = "id", nullable = false, length = 16)
    private String id;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CenterId entity = (CenterId) o;
        return Objects.equals(this.locationId, entity.locationId) &&
                Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, id);
    }

}