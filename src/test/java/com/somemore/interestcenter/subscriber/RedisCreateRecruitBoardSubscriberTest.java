package com.somemore.interestcenter.subscriber;

import com.somemore.interestcenter.event.converter.CreateRecruitBoardMessageConverter;
import com.somemore.interestcenter.event.subscriber.RedisCreateRecruitBoardSubscriber;
import com.somemore.interestcenter.event.handler.CreateRecruitBoardHandler;
import com.somemore.recruitboard.event.CreateRecruitBoardEvent;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import static org.mockito.Mockito.*;

class RedisCreateRecruitBoardSubscriberTest {

    @Mock
    private CreateRecruitBoardMessageConverter messageConverter;

    @Mock
    private CreateRecruitBoardHandler createRecruitBoardHandler;

    @InjectMocks
    private RedisCreateRecruitBoardSubscriber subscriber;

    @Test
    void onMessage_ShouldProcessMessage() {
        // given
        MockitoAnnotations.openMocks(this);

        String messageBody = """
                {
                    "type": "DOMAIN_EVENT",
                    "subType": "CREATE_RECRUIT_BOARD",
                    "centerId": "123e4567-e89b-12d3-a456-426614174001",
                    "recruitBoardId": 456
                }
                """;

        byte[] bodyBytes = messageBody.getBytes();

        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(bodyBytes);

        CreateRecruitBoardEvent expectedEvent = mock(CreateRecruitBoardEvent.class);

        when(messageConverter.from(messageBody)).thenReturn(expectedEvent);

        // when
        subscriber.onMessage(redisMessage, null);

        // then
        verify(messageConverter, times(1)).from(messageBody);
        verify(createRecruitBoardHandler, times(1)).handle(expectedEvent);
    }
}
