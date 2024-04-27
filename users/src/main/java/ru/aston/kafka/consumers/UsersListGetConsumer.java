package ru.aston.kafka.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

        Gson gson = new Gson();
        List<Long> userIds = gson.fromJson(message, new TypeToken<List<Long>>() {}.getType());

        List<UserDto> userDtoList = userService.getUserListByIds(userIds);

        usersListGetProducer.sendMessage(userDtoList);
    }
}
