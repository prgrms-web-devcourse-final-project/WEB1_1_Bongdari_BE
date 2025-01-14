package com.somemore.domains.volunteerrecord.event.subscriber;

import com.somemore.domains.volunteerrecord.domain.VolunteerRecord;
import com.somemore.domains.volunteerrecord.event.convert.VolunteerRecordMessageConverter;
import com.somemore.domains.volunteerrecord.event.handler.SettleVolunteerHoursHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSettleVolunteerHoursSubscriber implements MessageListener {

    private final SettleVolunteerHoursHandler settleVolunteerHoursHandler;
    private final VolunteerRecordMessageConverter messageConverter;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        VolunteerRecord volunteerRecord = messageConverter.from(
                new String(message.getBody())
        );

        settleVolunteerHoursHandler.handle(volunteerRecord);
    }
}
