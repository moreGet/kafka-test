package com.ch.kafkatest.service;

import com.ch.kafkatest.dto.ProduceRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageConsumer {

    private final List<ProduceRes.Message> messages = new ArrayList<>();
    private final ObjectMapper om;

    /**
     * 필수 구현 메소드
     */
    @KafkaListener(topics = "test", groupId = "ai")
    public void consume(String message) {
        try {
            ProduceRes.Message msg = om.readValue(message, ProduceRes.Message.class);
            synchronized (messages) {
                messages.add(msg);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void processMessages() {
        synchronized (messages) {
            if (!messages.isEmpty()) {
                messages.forEach(msg -> log.debug("Receive message : {} {}", msg.getContent(), msg.getTimestamp()));
                messages.clear();
            }
        }
    }
}
