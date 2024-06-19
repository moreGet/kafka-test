package com.ch.kafkatest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProduceRes {

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Message {
        private String content;
        private LocalDateTime timestamp;

        public Message(String content, LocalDateTime timestamp) {
            this.content = content;
            this.timestamp = timestamp;
        }
    }
}
