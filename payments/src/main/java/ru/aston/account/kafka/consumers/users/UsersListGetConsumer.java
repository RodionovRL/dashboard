package ru.aston.account.kafka.consumers.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.account.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UsersListGetConsumer {
    private List<UserDto> userDtoList;
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "users-list-get-response", groupId = "orders-group")
    public void consume(String message) {
        try {
            List<UserDto> receivedUserDtoList = objectMapper.readValue(message, new TypeReference<>() {});
            userDtoList.addAll(receivedUserDtoList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create UserDto list from Json");
        }
    }

    public List<UserDto> getReceivedUserDtoList() {
        List<UserDto> currentUserDtoList = new ArrayList<>(this.userDtoList);
        this.userDtoList.clear();
        return currentUserDtoList;
    }
}
