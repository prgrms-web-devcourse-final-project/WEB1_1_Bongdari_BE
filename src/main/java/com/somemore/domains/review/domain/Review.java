package com.somemore.domains.review.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "volunteer_apply_id", nullable = false)
    private Long volunteerApplyId;

    @Column(name = "volunteer_id", nullable = false, length = 16)
    private UUID volunteerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Builder
    public Review(Long volunteerApplyId, UUID volunteerId, String title,
            String content, String imgUrl) {
        this.volunteerApplyId = volunteerApplyId;
        this.volunteerId = volunteerId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public boolean isWriter(UUID volunteerId) {
        return this.volunteerId.equals(volunteerId);
    }

    public void updateWith(ReviewUpdateRequestDto dto) {
        this.title = dto.title();
        this.content = dto.content();
    }

    public void updateWith(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
