package com.somemore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Lob
    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "requirement")
    private String requirement;

    @Column(name = "homepage_link")
    private String homepageLink;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "account_pw", nullable = false)
    private String accountPw;

}