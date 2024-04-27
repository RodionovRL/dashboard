package ru.aston.kafka.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.UserDto;
import ru.aston.kafka.producers.UsersListGetProducer;
import ru.aston.service.UserService;

import java.util.List;

@Component
@AllArgsConstructor
public class UsersListGetConsumer {
    private UserService userService;
    private ObjectMapper objectMapper;
    private UsersListGetProducer usersListGetProducer;

    @KafkaListener(topics = "users-list-get-request", groupId = "users-group")
    public void consume(String message) {
        List<Long> userIds;
        try {
            userIds = objectMapper.readValue(message, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing List<Long> from message");
        }
        List<UserDto> userDtoList = userService.getUserListByIds(userIds);
        usersListGetProducer.sendMessage(userDtoList);
    }
}