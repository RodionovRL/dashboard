package ru.aston.kafka.producers.user;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserGetProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Long id) {
       kafkaTemplate.send("user-get-request", String.valueOf(id));
    }
}