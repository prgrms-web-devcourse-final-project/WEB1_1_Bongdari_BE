package com.somemore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class InterestCenterId implements java.io.Serializable {
    private static final long serialVersionUID = -5425532132170549854L;
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "volunteer_id", nullable = false, length = 16)
    private String volunteerId;

    @Column(name = "center_id", nullable = false, length = 16)
    private String centerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InterestCenterId entity = (InterestCenterId) o;
        return Objects.equals(this.centerId, entity.centerId) &&
                Objects.equals(this.id, entity.id) &&
                Objects.equals(this.volunteerId, entity.volunteerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerId, id, volunteerId);
    }

}