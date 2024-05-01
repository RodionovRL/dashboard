package ru.aston.payment.kafka.produsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.payment.dto.PaymentDto;

/**
 * Service class for producing messages with user json data to Kafka topic.
 * This service sends messages with user json data to the "user-get-response" Kafka topic.
 */
@Service
@AllArgsConstructor
public class PaymentProducer {

    /**
     * Logger for the {@link PaymentProducer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentProducer.class);

    /**
     * KafkaTemplate for sending messages to Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    /**
     * Sends a message with user json data to the "payment-response" Kafka topic.
     * @param paymentDto The user DTO to send.
     */
    public void sendMessage(PaymentDto paymentDto) {
        String paymentJson;
        try {
            paymentJson = objectMapper.writeValueAsString(paymentDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to create json from paymentDto {}", paymentDto, e);
            throw new RuntimeException("Failed to create json from userDto");
        }
        kafkaTemplate.send("payment-response", paymentJson);
        log.debug("Sent response to Kafka with payment data. Topic: payment-response , payment: {}", paymentJson);
    }
}