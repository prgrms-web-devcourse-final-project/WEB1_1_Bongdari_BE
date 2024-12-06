package com.somemore.center.domain;

import com.somemore.center.dto.request.CenterProfileUpdateRequestDto;
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

    @Column(name = "introduce", columnDefinition = "TEXT", nullable = false)
    private String introduce;

    @Column(name = "homepage_link", nullable = false)
    private String homepageLink;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "account_pw", nullable = false)
    private String accountPw;


    @Builder
    private Center(String name, String contactNumber, String imgUrl, String introduce, String homepageLink, String accountId, String accountPw) {

        this.name = name;
        this.contactNumber = contactNumber;
        this.imgUrl = imgUrl;
        this.introduce = introduce;
        this.homepageLink = homepageLink;
        this.accountId = accountId;
        this.accountPw = accountPw;
    }

    public static Center create(String name, String contactNumber, String imgUrl, String introduce, String homepageLink, String accountId, String accountPw) {
        return Center.builder()
                .name(name)
                .contactNumber(contactNumber)
                .imgUrl(imgUrl)
                .introduce(introduce)
                .homepageLink(homepageLink)
                .accountId(accountId)
                .accountPw(accountPw)
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
