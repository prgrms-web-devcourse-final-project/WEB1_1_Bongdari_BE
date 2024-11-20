package com.somemore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Volunteer {
    @Id
    @Column(name = "id", nullable = false, length = 16)
    private String id;

    @Column(name = "oauth_provider", nullable = false)
    private String oauthProvider;

    @Column(name = "oauth_id", nullable = false)
    private String oauthId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Lob
    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "tier", nullable = false, length = 20)
    private String tier;

    @Column(name = "total_volunteer_hours", nullable = false)
    private Integer totalVolunteerHours;

    @Column(name = "total_volunteer_count", nullable = false)
    private Integer totalVolunteerCount;

}