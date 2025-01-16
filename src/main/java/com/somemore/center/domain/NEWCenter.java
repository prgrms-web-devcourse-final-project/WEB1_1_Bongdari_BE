package com.somemore.center.domain;

import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "new_center") // TODO prefix 삭제
public class NEWCenter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "homepage_url", nullable = false)
    private String homepageUrl;

    @Builder
    private NEWCenter(UUID userId, String name, String homepageUrl) {
        this.userId = userId;
        this.homepageUrl = homepageUrl;
    }

    public static NEWCenter createDefault(UUID userId) {
        return NEWCenter.builder()
                .userId(userId)
                .homepageUrl("")
                .build();
    }
}
