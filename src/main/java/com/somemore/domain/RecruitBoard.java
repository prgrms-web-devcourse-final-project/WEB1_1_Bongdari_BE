package com.somemore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Recruit_board")
public class RecruitBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "recruit_status", nullable = false, length = 20)
    private String recruitStatus;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "volunteer_date", nullable = false)
    private Instant volunteerDate;

    @Column(name = "volunteer_type", nullable = false, length = 20)
    private String volunteerType;

    @Column(name = "volunteer_hours", nullable = false)
    private Integer volunteerHours;

    @Column(name = "admitted", nullable = false)
    private Boolean admitted = false;

}