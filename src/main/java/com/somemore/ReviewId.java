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
public class ReviewId implements java.io.Serializable {
    private static final long serialVersionUID = 2589769979111985676L;
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "volunteer_id", nullable = false, length = 16)
    private String volunteerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewId entity = (ReviewId) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.volunteerId, entity.volunteerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, volunteerId);
    }

}