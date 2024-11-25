package com.somemore.community.dto.response;

import com.somemore.volunteer.domain.Tier;

import java.util.UUID;

public interface WriterDetailDto {
    UUID id();
    String name();
    String imgUrl();
    Tier tier();
}
