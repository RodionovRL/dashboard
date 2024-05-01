package ru.aston.kafka.consumers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Kafka consumer component for receiving a list of user data from Kafka topic.
 * This component listens to the "users-list-get-response" Kafka topic
 * and consumes messages containing a list of user data.
 */
@Component
@AllArgsConstructor
public class UsersListGetConsumer {

    /**
     * Logger for the {@link UsersListGetConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UsersListGetConsumer.class);

    /**
     * The list to store the received list of users data.
     */
    private List<UserDto> userDtoList;

    /**
     * ObjectMapper for deserializing JSON messages.
     */
    private ObjectMapper objectMapper;

    /**
     * Kafka listener method to consume messages from the "users-list-get-response" topic.
     * @param message The JSON message containing a list of user data.
     */
    @KafkaListener(topics = "users-list-get-response", groupId = "orders-group")
    public void consume(String message) {
        try {
            List<UserDto> receivedUserDtoList = objectMapper.readValue(message, new TypeReference<>() {});
            userDtoList.addAll(receivedUserDtoList);
            log.debug("Received users list from Users service: {}", userDtoList);
        } catch (JsonProcessingException e) {
            log.error("Failed to create UserDto list from Json. Incoming message: {}, Error: {}", message, e);
            throw new RuntimeException("Failed to create UserDto list from Json");
        }
    }

    /**
     * Retrieves the received list of UserDto objects.
     * Clears the internal list after retrieval to ensure the next call returns an empty list
     * if no new data received.
     * @return The received list of UserDto objects.
     */
    public List<UserDto> getReceivedUserDtoList() {
        List<UserDto> currentUserDtoList = new ArrayList<>(this.userDtoList);
        this.userDtoList.clear();
        log.debug("Retrieved UserDto list from Kafka: {}", currentUserDtoList);
        return currentUserDtoList;
    }
}