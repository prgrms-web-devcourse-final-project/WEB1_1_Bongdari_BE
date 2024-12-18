package com.somemore.support.fixture;

import com.somemore.domains.center.domain.Center;

public class CenterFixture {

    private CenterFixture() {
    }

    public static Center createCenter() {
        return Center.builder()
            .name("센터 이름")
            .contactNumber("010-1111-1111")
            .imgUrl("https://image.domain.com/center-img")
            .introduce("센터 소개")
            .homepageLink("https://www.centerhomepage.com")
            .accountId("center_account")
            .accountPw("password123")
            .build();
    }

    public static Center createCenter(String name) {
        return Center.builder()
            .name(name)
            .contactNumber("010-1111-1111")
            .imgUrl("https://image.domain.com/center-img")
            .introduce("센터 소개")
            .homepageLink("https://www.centerhomepage.com")
            .accountId("center_account")
            .accountPw("password123")
            .build();
    }

}
