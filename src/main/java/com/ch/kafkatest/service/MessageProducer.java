package com.ch.kafkatest.service;

import com.ch.kafkatest.dto.ProduceReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper om;

    // 카프카 서버에서 발행한 토픽(채널) 이름
    private static final String TOPIC = "test";

    private int messageCount = 0;

    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
        ProduceReq.Message message = new ProduceReq.Message();
        message.setContent("Hello Kotech - " + messageCount);
        message.setTimestamp(LocalDateTime.now());
        messageCount++;

        try {
            String jsonMsg = om.writeValueAsString(message);
            kafkaTemplate.send(TOPIC, jsonMsg);
            log.debug("Sent message: {}", jsonMsg);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert message to JSON: {}", e.getMessage(), e);
        }
    }
}
