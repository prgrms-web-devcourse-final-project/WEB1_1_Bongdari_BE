package com.somemore.location.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.somemore.global.common.BaseEntity;
import com.somemore.location.dto.request.LocationUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "location")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 12, scale = 8)
    private BigDecimal longitude;

    @Builder
    public Location(String address, BigDecimal latitude, BigDecimal longitude) {
        this.address = address;
        this.latitude = latitude.setScale(8, RoundingMode.HALF_UP);
        this.longitude = longitude.setScale(8, RoundingMode.HALF_UP);
    }

    public void updateWith(LocationUpdateRequestDto requestDto) {
        this.address = requestDto.address();
        this.latitude = requestDto.latitude().setScale(8, RoundingMode.HALF_UP);
        this.longitude = requestDto.longitude().setScale(8, RoundingMode.HALF_UP);
    }
}