package com.somemore.common.fixture;

import com.somemore.location.domain.Location;
import java.math.BigDecimal;

public class LocationFixture {

    public static Location createLocation() {
        return Location.builder()
            .address("주소주소")
            .latitude(BigDecimal.valueOf(37.5665))
            .longitude(BigDecimal.valueOf(126.9780))
            .build();
    }

    public static Location createLocation(String address) {
        return Location.builder()
            .address(address)
            .latitude(BigDecimal.valueOf(37.5665))
            .longitude(BigDecimal.valueOf(126.9780))
            .build();
    }

    public static Location createLocation(BigDecimal latitude, BigDecimal longitude) {
        return Location.builder()
            .address("주소주소")
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

    public static Location createLocation(String address, BigDecimal latitude,
        BigDecimal longitude) {
        return Location.builder()
            .address(address)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }
}
