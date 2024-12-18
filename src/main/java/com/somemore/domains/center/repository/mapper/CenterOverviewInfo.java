package com.somemore.domains.center.repository.mapper;


import java.util.UUID;

public record CenterOverviewInfo(
        UUID centerId,
        String centerName,
        String imgUrl
) {

}
