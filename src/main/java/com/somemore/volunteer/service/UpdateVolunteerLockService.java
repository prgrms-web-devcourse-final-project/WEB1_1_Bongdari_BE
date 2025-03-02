package com.somemore.volunteer.service;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;

import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.usecase.UpdateVolunteerUseCase;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateVolunteerLockService implements UpdateVolunteerUseCase {

    private final NEWVolunteerRepository volunteerRepository;
    private final RedissonClient redissonClient;

    @Override
    public void updateVolunteerStats(UUID id, int hours) {
        RLock lock = redissonClient.getLock(id.toString());

        try {
            boolean available = lock.tryLock(
                    1, 3, TimeUnit.SECONDS
            );

            if (!available) {
                log.info("lock 획득 실패");
                return;
            }
            log.info("lock 획득 성공");
            updateStatsWithLock(id, hours);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("lock 해제");
            lock.unlock();
        }
    }

    private void updateStatsWithLock(UUID id, int hours) {
        NEWVolunteer volunteer = volunteerRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_EXISTS_VOLUNTEER)
        );

        volunteer.updateVolunteerStats(hours, 1);
        volunteerRepository.save(volunteer);
    }
}
