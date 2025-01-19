package com.somemore.domains.volunteerrecord.repository.mapper;

import com.somemore.domains.volunteerrecord.dto.response.VolunteerMonthlyRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerTotalRankingResponseDto;
import com.somemore.domains.volunteerrecord.dto.response.VolunteerWeeklyRankingResponseDto;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

public class VolunteerRankingMapper {

    private VolunteerRankingMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static VolunteerTotalRankingResponseDto toTotalRankingResponse(Object[] result, Map<UUID, String> nicknameMap) {
        UUID volunteerId = toUUID(result[0]);
        return new VolunteerTotalRankingResponseDto(
                volunteerId,
                ((Number) result[1]).intValue(),
                ((Number) result[2]).longValue(),
                nicknameMap.get(volunteerId)
        );
    }

    public static VolunteerWeeklyRankingResponseDto toWeeklyRankingResponse(Object[] result, Map<UUID, String> nicknameMap) {
        UUID volunteerId = toUUID(result[0]);
        return new VolunteerWeeklyRankingResponseDto(
                volunteerId,
                ((Number) result[1]).intValue(),
                ((Number) result[2]).longValue(),
                nicknameMap.get(volunteerId)
        );
    }

    public static VolunteerMonthlyRankingResponseDto toMonthlyRankingResponse(Object[] result, Map<UUID, String> nicknameMap) {
        UUID volunteerId = toUUID(result[0]);
        return new VolunteerMonthlyRankingResponseDto(
                volunteerId,
                ((Number) result[1]).intValue(),
                ((Number) result[2]).longValue(),
                nicknameMap.get(volunteerId)
        );
    }

    public static UUID toUUID(Object uuidObject) {
        return switch (uuidObject) {
            case UUID uuid -> uuid;
            case byte[] bytes -> {
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                yield new UUID(bb.getLong(), bb.getLong());
            }
            default -> throw new IllegalArgumentException("UUID 변환이 불가능한 데이터 타입: " + uuidObject.getClass().getName());
        };
    }
}
