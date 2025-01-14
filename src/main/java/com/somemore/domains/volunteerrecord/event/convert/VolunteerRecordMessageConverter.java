package com.somemore.domains.volunteerrecord.event.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.domains.volunteerrecord.event.VolunteerRecordCreateEvent;
import com.somemore.global.common.event.DomainEventSubType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VolunteerRecordMessageConverter {

    private static final String SUB_TYPE = "subType";
    private final ObjectMapper objectMapper;

    public VolunteerRecord from(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String eventType = rootNode.get(SUB_TYPE).asText();

            return switch (DomainEventSubType.from(eventType)) {
                case VOLUNTEER_HOURS_SETTLE -> convertToVolunteerRecord(message);
                default -> {
                    log.error("지원하지 않는 이벤트 타입입니다: {}", eventType);
                    throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
                }
            };
        } catch (Exception e) {
            log.error("메시지 변환 실패: {}", e.getMessage());
            throw new IllegalStateException("메시지 변환 중 오류가 발생했습니다.", e);
        }
    }

    private VolunteerRecord convertToVolunteerRecord(String message) throws JsonProcessingException {

        VolunteerRecordCreateEvent volunteerRecordCreateEvent = objectMapper.readValue(message, VolunteerRecordCreateEvent.class);

        return VolunteerRecord.create(
                volunteerRecordCreateEvent.getVolunteerId(),
                volunteerRecordCreateEvent.getTitle(),
                volunteerRecordCreateEvent.getVolunteerDate(),
                volunteerRecordCreateEvent.getVolunteerHours()
        );

    }
}
