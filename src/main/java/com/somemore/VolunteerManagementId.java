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
public class VolunteerManagementId implements java.io.Serializable {
    private static final long serialVersionUID = -4453922091554162671L;
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "volunteer_id", nullable = false, length = 16)
    private String volunteerId;

    @Column(name = "recruit_boad_id", nullable = false)
    private Long recruitBoadId;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VolunteerManagementId entity = (VolunteerManagementId) o;
        return Objects.equals(this.recruitBoadId, entity.recruitBoadId) &&
                Objects.equals(this.id, entity.id) &&
                Objects.equals(this.volunteerId, entity.volunteerId) &&
                Objects.equals(this.reviewId, entity.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recruitBoadId, id, volunteerId, reviewId);
    }

}