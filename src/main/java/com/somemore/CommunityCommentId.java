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
public class CommunityCommentId implements java.io.Serializable {
    private static final long serialVersionUID = -3100783583496863893L;
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "comunity_board_id", nullable = false)
    private Long comunityBoardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommunityCommentId entity = (CommunityCommentId) o;
        return Objects.equals(this.comunityBoardId, entity.comunityBoardId) &&
                Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comunityBoardId, id);
    }

}