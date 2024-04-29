package ru.aston.kafka.consumers.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer component for receiving user data from Kafka topic.
 * This component listens to the "user-get-response" Kafka topic and consumes messages containing user data.
 */
@Component
@AllArgsConstructor
public class UserGetConsumer {

    /**
     * Logger for the {@link UserGetConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserGetConsumer.class);
    private UserDto userDto;
    private ObjectMapper objectMapper;

    /**
     * Kafka listener method to consume messages from the "user-get-response" topic.
     * @param message The JSON message containing user data.
     */
    @KafkaListener(topics = "user-get-response", groupId = "orders-group")
    public void consume(String message) {
        try {
            this.userDto = objectMapper.readValue(message, UserDto.class);
            log.debug("Received user data from Users service: {}", userDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to create UserDto from Json. Incoming message: {}", message, e);
            throw new RuntimeException("Failed to create UserDto from Json");
        }
    }

    /**
     * Retrieves the received UserDto.
     * Resets the userDto field to null after retrieval to ensure the next call returns null if no new data received.
     * @return The received UserDto, or null if no data received.
     */
    public UserDto getReceivedUserDto() {
        UserDto currentUserDto = this.userDto;
        this.userDto = null;
        log.debug("Retrieved UserDto from Kafka: {}", currentUserDto);
        return currentUserDto;
    }
}