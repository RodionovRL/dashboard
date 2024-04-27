package ru.aston.kafka.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersListGetProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public void sendMessage(List<UserDto> usersList) {
        String usersListJson;
        try {
            usersListJson = objectMapper.writeValueAsString(usersList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create json from userDto list");
        }
        kafkaTemplate.send("users-list-get-response", usersListJson);
    }
}