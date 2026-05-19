package com.example.toeicwebsite.infrastucture.redis.listener;

import com.example.toeicwebsite.application.service.ForceSubmitExpiredExamImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class ExamTimeoutListener {

    private final RedissonClient redissonClient;
    private final ForceSubmitExpiredExamImpl forceSubmitExpiredExam;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        executorService.submit(() -> {
            RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("examTimeoutQueue");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String attemptId = blockingQueue.take();
                    try {
                        forceSubmitExpiredExam.forceSubmit(attemptId);
                    } catch (Exception e) {
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        });
    }
}
