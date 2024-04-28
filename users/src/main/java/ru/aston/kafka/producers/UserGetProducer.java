package ru.aston.kafka.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.dto.UserDto;

@Service
@AllArgsConstructor
public class UserGetProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public void sendMessage(UserDto userDto) {
        String userJson;
        try {
            userJson = objectMapper.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create json from userDto");
        }
        kafkaTemplate.send("user-get-response", userJson);
    }
}