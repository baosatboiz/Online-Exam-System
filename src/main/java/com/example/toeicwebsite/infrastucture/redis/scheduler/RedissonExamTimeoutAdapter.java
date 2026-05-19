package com.example.toeicwebsite.infrastucture.redis.scheduler;

import com.example.toeicwebsite.application.port.ExamTimeoutSchedulerPort;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonExamTimeoutAdapter implements ExamTimeoutSchedulerPort {

    private final RedissonClient redissonClient;

    @Override
    public void scheduleTimeout(ExamAttemptId attemptId, int delayInMinutes) {
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(redissonClient.getQueue("examTimeoutQueue"));
        delayedQueue.offer(attemptId.value().toString(), delayInMinutes, TimeUnit.MINUTES);
    }
}
