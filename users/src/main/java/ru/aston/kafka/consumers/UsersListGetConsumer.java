package ru.aston.kafka.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.UserDto;
import ru.aston.kafka.producers.UsersListGetProducer;
import ru.aston.service.UserService;

import java.util.List;

/**
 * Kafka consumer component for receiving user IDs list from Kafka topic.
 * This component listens to the "users-list-get-request" Kafka topic
 * and consumes messages containing user IDs list.
 */
@Component
@AllArgsConstructor
public class UsersListGetConsumer {

    /**
     * Logger for the {@link UsersListGetConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UsersListGetConsumer.class);
    private UserService userService;
    private ObjectMapper objectMapper;
    private UsersListGetProducer usersListGetProducer;

    /**
     * Kafka listener method to consume messages from the "users-list-get-request" topic.
     * @param message The message containing list of user IDs.
     */
    @KafkaListener(topics = "users-list-get-request", groupId = "users-group")
    public void consume(String message) {
        List<Long> userIds;
        try {
            userIds = objectMapper.readValue(message, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error deserializing user IDs list from message. User IDs: {}, Error: {}",
                    message, e);
            throw new RuntimeException("Error deserializing List<Long> from message");
        }
        log.debug("Received user IDs list: {} from Orders service", userIds);
        List<UserDto> userDtoList = userService.getUserListByIds(userIds);
        usersListGetProducer.sendMessage(userDtoList);
    }
}