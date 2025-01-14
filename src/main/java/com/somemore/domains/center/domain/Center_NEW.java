package com.somemore.domains.center.domain;

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
@Table(name = "center_new") // TODO suffix 삭제
public class Center_NEW {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "homepage_url", nullable = false)
    private String homepageUrl;

    @Builder
    private Center_NEW(UUID userId, String name, String homepageUrl) {
        this.userId = userId;
        this.name = name;
        this.homepageUrl = homepageUrl;
    }

    public static Center_NEW create(UUID userId,
                                    String name,
                                    String homepageUrl) {
        return Center_NEW.builder()
                .userId(userId)
                .name(name)
                .homepageUrl(homepageUrl)
                .build();
    }
}
