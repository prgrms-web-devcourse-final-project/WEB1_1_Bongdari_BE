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
public class Center {
    @EmbeddedId
    private CenterId id;

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