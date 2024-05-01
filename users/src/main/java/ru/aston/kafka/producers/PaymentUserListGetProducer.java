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

@Service
@AllArgsConstructor
public class PaymentUserListGetProducer {
    /**
     * Logger for the {@link PaymentUserListGetProducer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentUserListGetProducer.class);

    /**
     * KafkaTemplate for sending messages to Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    /**
     * Sends a message with users list json data to the "payment-user-list-get-response" Kafka topic.
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
        kafkaTemplate.send("payment-user-list-get-response", usersListJson);
        log.debug("Sent response to Kafka with users list data. Topic: payment-user-list-get-response , " +
                "usersList: {}", usersListJson);
    }
}
