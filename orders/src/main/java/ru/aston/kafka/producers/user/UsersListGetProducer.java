package ru.aston.kafka.producers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for producing messages related to batch user retrieval to a Kafka topic.
 * This service sends messages to the "users-list-get-request" Kafka topic for requesting user data in batch.
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

    /**
     * ObjectMapper for serializing Java objects to JSON.
     */
    private ObjectMapper objectMapper;

    /**
     * Sends a message to the "users-list-get-request" Kafka topic for requesting batch users data.
     * @param idList The list of IDs of users to retrieve.
     */
    public void sendMessage(List<Long> idList) {
        String ids;
        try {
            ids = objectMapper.writeValueAsString(idList);
        } catch (JsonProcessingException e) {
            log.error("Failed to create json from id list. Input list: {} , Error: {}", idList, e);
            throw new RuntimeException("Failed to create json from id list");
        }
        kafkaTemplate.send("users-list-get-request", ids);
        log.debug("Sent request to Kafka to retrieve users list. " +
                "Topic: users-list-get-request, User IDs: {}", ids);
    }
}