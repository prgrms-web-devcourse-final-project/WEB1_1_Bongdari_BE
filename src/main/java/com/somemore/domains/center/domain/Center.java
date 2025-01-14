package com.somemore.domains.center.domain;

import com.somemore.domains.center.dto.request.CenterProfileUpdateRequestDto;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Center extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Lob
    @Column(name = "introduce", length = 500, nullable = false)
    private String introduce;

    @Column(name = "homepage_link", nullable = false)
    private String homepageLink;

    @Builder
    private Center(String name, String contactNumber, String imgUrl, String introduce, String homepageLink) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.imgUrl = imgUrl;
        this.introduce = introduce;
        this.homepageLink = homepageLink;
    }

    public static Center create(String name, String contactNumber, String imgUrl, String introduce, String homepageLink) {
        return Center.builder()
                .name(name)
                .contactNumber(contactNumber)
                .imgUrl(imgUrl)
                .introduce(introduce)
                .homepageLink(homepageLink)
                .build();
    }

    public void updateWith(CenterProfileUpdateRequestDto dto, String imgUrl) {
        this.name = dto.name();
        this.contactNumber = dto.contactNumber();
        this.homepageLink = dto.homepageLink();
        this.introduce = dto.introduce();
        this.imgUrl = imgUrl;
    }
}
