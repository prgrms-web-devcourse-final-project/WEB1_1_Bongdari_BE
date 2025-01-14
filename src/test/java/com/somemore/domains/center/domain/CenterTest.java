package com.somemore.domains.center.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CenterTest {

    private static final String NAME = "서울 도서관";
    private static final String CONTACT_NUMBER = "02-1234-5678";
    private static final String IMG_URL = "http://example.com/image.jpg";
    private static final String INTRODUCE = "우리는 서울 도서관입니다.";
    private static final String HOMEPAGE_LINK = "http://testcenter.com";

    @DisplayName("Center 엔티티를 생성할 수 있다.")
    @Test
    void create() {
        //when
        Center center = Center.create(
                NAME,
                CONTACT_NUMBER,
                IMG_URL,
                INTRODUCE,
                HOMEPAGE_LINK
        );

        //then
        assertAll(
                () -> assertThat(center.getName()).isEqualTo(NAME),
                () -> assertThat(center.getContactNumber()).isEqualTo(CONTACT_NUMBER),
                () -> assertThat(center.getImgUrl()).isEqualTo(IMG_URL),
                () -> assertThat(center.getIntroduce()).isEqualTo(INTRODUCE),
                () -> assertThat(center.getHomepageLink()).isEqualTo(HOMEPAGE_LINK)
        );
    }


}