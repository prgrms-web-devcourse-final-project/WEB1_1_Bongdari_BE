package com.somemore.center.repository.record;


import java.util.UUID;

public record CenterOverviewInfo(
        UUID centerId,
        String centerName,
        String imgUrl
) {

}
