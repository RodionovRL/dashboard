package ru.aston.kafka.producers.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aston.dto.PaymentRequestDto;

@Service
@AllArgsConstructor
public class PaymentRequestProducer {

    /**
     * Logger for the {@link PaymentRequestProducer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentRequestProducer.class);

    /**
     * KafkaTemplate for sending messages to Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;

    /**
     * Sends a message to the "payment-request" Kafka topic for requesting payment.
     * @param paymentDto The payment DTO of the payment to make.
     */
    public void sendMessage(PaymentRequestDto paymentDto) {
        String paymentJson;
        try {
            paymentJson = objectMapper.writeValueAsString(paymentDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to create json from paymentDto {}", paymentDto, e);
            throw new RuntimeException("Failed to create json from paymentDto");
        }
        kafkaTemplate.send("payment-request", paymentJson);
        log.debug("Sent request to Kafka to make payment. Topic: payment-request , payment request: {}",
                paymentJson);
    }
}
