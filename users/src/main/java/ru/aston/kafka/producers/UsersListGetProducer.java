package ru.aston.kafka.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.dto.UserDto;

import java.util.List;

/**
 * Service class for producing messages with users list json data to Kafka topic.
 * This service sends messages with users list json data to the "users-list-get-response" Kafka topic.
 */
@Service
@AllArgsConstructor
public class UsersListGetProducer {

    /**
     * Logger for the {@link UsersListGetProducer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UsersListGetProducer.class);

    /**
     * KafkaTemplate for sending messages to Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    /**
     * Sends a message with users list json data to the "users-list-get-response" Kafka topic.
     * @param usersList The users DTO list to send.
     */
    public void sendMessage(List<UserDto> usersList) {
        String usersListJson;
        try {
            usersListJson = objectMapper.writeValueAsString(usersList);
        } catch (JsonProcessingException e) {
            log.error("Failed to create json from userDto list: {}, Error: {}", usersList, e);
            throw new RuntimeException("Failed to create json from userDto list");
        }
        kafkaTemplate.send("users-list-get-response", usersListJson);
        log.debug("Sent response to Kafka with users list data. Topic: users-list-get-response , " +
                "usersList: {}", usersListJson);
    }
}