package ru.aston.kafka.producers.user;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for producing messages related to user retrieval to a Kafka topic.
 * This service sends messages to the "user-get-request" Kafka topic for requesting user data.
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

    /**
     * Sends a message to the "user-get-request" Kafka topic for requesting user data.
     * @param id The ID of the user to retrieve.
     */
    public void sendMessage(Long id) {
        kafkaTemplate.send("user-get-request", String.valueOf(id));
        log.debug("Sent request to Kafka to retrieve user data. Topic: user-get-request , user ID : {}", id);
    }
}