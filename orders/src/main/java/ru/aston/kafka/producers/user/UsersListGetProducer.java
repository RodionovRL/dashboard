package ru.aston.kafka.producers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersListGetProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public void sendMessage(List<Long> idList) {
        String ids;

        try {
            ids = objectMapper.writeValueAsString(idList);
        } catch (JsonProcessingException e) {
           throw new RuntimeException("Failed to create json from id list");
        }

        kafkaTemplate.send("users-list-get-request", ids);
    }
}
