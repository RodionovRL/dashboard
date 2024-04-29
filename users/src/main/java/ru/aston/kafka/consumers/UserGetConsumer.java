package ru.aston.kafka.consumers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.UserDto;
import ru.aston.kafka.producers.UserGetProducer;
import ru.aston.service.UserService;

/**
 * Kafka consumer component for receiving user id from Kafka topic.
 * This component listens to the "user-get-request" Kafka topic and consumes messages containing user id.
 */
@Component
@AllArgsConstructor
public class UserGetConsumer {

    /**
     * Logger for the {@link UserGetConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserGetConsumer.class);
    private UserService userService;
    private UserGetProducer userGetProducer;

    /**
     * Kafka listener method to consume messages from the "user-get-request" topic.
     * @param message The message containing user id.
     */
    @KafkaListener(topics = "user-get-request", groupId = "users-group")
    public void consume(String message) {
        Long userId = Long.valueOf(message);
        log.debug("Received user ID: {} from Orders service", userId);
        UserDto userDto = userService.getUserById(userId);
        userGetProducer.sendMessage(userDto);
    }
}