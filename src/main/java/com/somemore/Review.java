package com.somemore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Review {
    @EmbeddedId
    private ReviewId id;

    @Column(name = "center_id", nullable = false, length = 16)
    private String centerId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "img_url")
    private String imgUrl;

}