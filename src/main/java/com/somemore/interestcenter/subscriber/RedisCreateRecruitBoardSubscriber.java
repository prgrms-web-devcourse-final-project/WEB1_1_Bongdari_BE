package com.somemore.interestcenter.subscriber;

import com.somemore.interestcenter.converter.CreateRecruitBoardMessageConverter;
import com.somemore.interestcenter.handler.CreateRecruitBoardHandler;
import com.somemore.recruitboard.event.CreateRecruitBoardEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCreateRecruitBoardSubscriber implements MessageListener {

    private final CreateRecruitBoardHandler createRecruitBoardHandler;
    private final CreateRecruitBoardMessageConverter messageConverter;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CreateRecruitBoardEvent event = messageConverter.from(
                new String(message.getBody())
        );

        createRecruitBoardHandler.handle(event);
    }
}
