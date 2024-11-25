package com.somemore.center.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CenterTest {

    private static final String NAME = "서울 도서관";
    private static final String CONTACT_NUMBER = "02-1234-5678";
    private static final String IMG_URL = "http://example.com/image.jpg";
    private static final String INTRODUCE = "우리는 서울 도서관입니다.";
    private static final String HOMEPAGE_LINK = "http://testcenter.com";
    private static final String ACCOUNT_ID = "testId";
    private static final String ACCOUNT_PW = "testPw";

    @DisplayName("Center 엔티티를 생성할 수 있다.")
    @Test
    void create() {
        //when
        Center center = Center.create(
                NAME,
                CONTACT_NUMBER,
                IMG_URL,
                INTRODUCE,
                HOMEPAGE_LINK,
                ACCOUNT_ID,
                ACCOUNT_PW
        );

        //then
        assertAll(
                () -> assertThat(center.getName()).isEqualTo(NAME),
                () -> assertThat(center.getContactNumber()).isEqualTo(CONTACT_NUMBER),
                () -> assertThat(center.getImgUrl()).isEqualTo(IMG_URL),
                () -> assertThat(center.getIntroduce()).isEqualTo(INTRODUCE),
                () -> assertThat(center.getHomepageLink()).isEqualTo(HOMEPAGE_LINK),
                () -> assertThat(center.getAccountId()).isEqualTo(ACCOUNT_ID),
                () -> assertThat(center.getAccountPw()).isEqualTo(ACCOUNT_PW)
        );
    }


}