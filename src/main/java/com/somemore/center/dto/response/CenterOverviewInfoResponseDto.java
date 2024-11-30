package com.somemore.center.dto.response;


import java.util.UUID;

public record CenterOverviewInfoResponseDto(
        UUID centerId,
        String centerName,
        String imgUrl
) {

}
