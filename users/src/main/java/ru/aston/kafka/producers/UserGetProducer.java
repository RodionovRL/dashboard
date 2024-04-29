package ru.aston.kafka.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.dto.UserDto;

/**
 * Service class for producing messages with user json data to Kafka topic.
 * This service sends messages with user json data to the "user-get-response" Kafka topic.
 */
@Service
@AllArgsConstructor
public class UserGetProducer {

    /**
     * Logger for the {@link UserGetProducer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserGetProducer.class);

    /**
     * KafkaTemplate for sending messages to Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    /**
     * Sends a message with user json data to the "user-get-response" Kafka topic.
     * @param userDto The user DTO to send.
     */
    public void sendMessage(UserDto userDto) {
        String userJson;
        try {
            userJson = objectMapper.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to create json from userDto {}", userDto, e);
            throw new RuntimeException("Failed to create json from userDto");
        }
        kafkaTemplate.send("user-get-response", userJson);
        log.debug("Sent response to Kafka with user data. Topic: user-get-response , user: {}", userJson);
    }
}