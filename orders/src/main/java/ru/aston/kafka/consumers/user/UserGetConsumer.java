package ru.aston.kafka.consumers.user;

import ru.aston.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserGetConsumer {
    private UserDto userDto;
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "user-get-response", groupId = "orders-group")
    public void consume(String message) {
        try {
            this.userDto = objectMapper.readValue(message, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create UserDto from Json");
        }
    }

    public UserDto getReceivedUserDto() {
        UserDto currentUserDto = this.userDto;
        this.userDto = null;
        return currentUserDto;
    }
}