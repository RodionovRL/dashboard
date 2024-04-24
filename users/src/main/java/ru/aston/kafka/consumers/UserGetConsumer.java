package ru.aston.kafka.consumers;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.UserDto;
import ru.aston.kafka.producers.UserGetProducer;
import ru.aston.service.UserService;

@Component
@AllArgsConstructor
public class UserGetConsumer {
    private UserService userService;
    UserGetProducer userGetProducer;

    @KafkaListener(topics = "user-get-request", groupId = "users-group")
    public void consume(String message) {
        Long userId = Long.valueOf(message);
        UserDto userDto = userService.getUserById(userId);
        userGetProducer.sendMessage(userDto);
    }
}