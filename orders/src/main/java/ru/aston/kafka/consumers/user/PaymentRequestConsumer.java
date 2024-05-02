package ru.aston.kafka.consumers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.PaymentDto;
import ru.aston.dto.UserDto;

@Component
@AllArgsConstructor
public class PaymentRequestConsumer {

    /**
     * Logger for the {@link PaymentRequestConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentRequestConsumer.class);
    private PaymentDto paymentDto;
    private ObjectMapper objectMapper;

    /**
     * Kafka listener method to consume messages from the "payment-response" topic.
     * @param message The JSON message containing payment data.
     */
    @KafkaListener(topics = "payment_response", groupId = "orders-group")
    public void consume(String message) {
        try {
            this.paymentDto = objectMapper.readValue(message, PaymentDto.class);
            log.debug("Received payment data from Payments service: {}", paymentDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to create PaymentDto from Json. Incoming message: {}", message, e);
            throw new RuntimeException("Failed to create PaymentDto from Json");
        }
    }

    /**
     * Retrieves the received PaymentDto.
     * Resets the paymentDto field to null after retrieval to ensure the next call returns null if no new data received.
     * @return The received PaymentDto, or null if no data received.
     */
    public PaymentDto getReceivedPaymentDto() {
        PaymentDto currentPaymentDto = this.paymentDto;
        this.paymentDto = null;
        log.debug("Retrieved UserDto from Kafka: {}", currentPaymentDto);
        return currentPaymentDto;
    }
}
