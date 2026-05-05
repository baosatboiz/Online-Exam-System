package com.example.toeicwebsite.infrastucture.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {

    private final AnswerStreamConsumer answerStreamConsumer;

    @Value("${app.redis.stream.exam-answer.key:exam_answer_stream}")
    private String streamKey;

    @Value("${app.redis.stream.exam-answer.group:exam_answer_group}")
    private String consumerGroup;

    @Value("${app.redis.stream.poll-timeout-ms:100}")
    private long pollTimeoutMs;

    @Bean(destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
            RedisConnectionFactory connectionFactory, StringRedisTemplate stringRedisTemplate) {

        // Ensure the stream key exists before creating consumer group
        // (createGroup fails if the stream doesn't exist yet)
        try {
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(streamKey))) {
                // Create a placeholder entry so the stream exists, then delete it
                stringRedisTemplate.opsForStream().add(
                        org.springframework.data.redis.connection.stream.StreamRecords
                                .newRecord()
                                .ofMap(java.util.Map.of("init", "true"))
                                .withStreamKey(streamKey)
                );
            }
        } catch (Exception e) {
            log.warn("Could not pre-initialize stream key '{}': {}", streamKey, e.getMessage());
        }

        // Create the consumer group – silently ignore if it already exists.
        // ReadOffset.latest() means new instances after a Redis flush won't
        // re-process old, already-persisted messages.
        try {
            stringRedisTemplate.opsForStream()
                    .createGroup(streamKey, ReadOffset.latest(), consumerGroup);
            log.info("Created Redis consumer group '{}' on stream '{}'", consumerGroup, streamKey);
        } catch (Exception e) {
            log.debug("Consumer group '{}' already exists (skipping creation): {}", consumerGroup, e.getMessage());
        }

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofMillis(pollTimeoutMs))
                        // Handle errors inside the listener gracefully; don't crash the container
                        .errorHandler(t -> log.error("Stream listener error: {}", t.getMessage(), t))
                        .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(connectionFactory, options);

        String consumerName = "consumer-1";
        try {
            consumerName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Could not resolve hostname, using fallback consumer name '{}'", consumerName);
        }

        container.receive(
                Consumer.from(consumerGroup, consumerName),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                answerStreamConsumer
        );

        container.start();
        log.info("Redis stream listener started: stream='{}', group='{}', consumer='{}'",
                streamKey, consumerGroup, consumerName);
        return container;
    }
}
