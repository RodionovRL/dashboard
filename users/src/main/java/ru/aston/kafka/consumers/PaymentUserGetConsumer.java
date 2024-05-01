package ru.aston.kafka.consumers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.dto.UserDto;
import ru.aston.kafka.producers.PaymentUserGetProducer;
import ru.aston.kafka.producers.UserGetProducer;
import ru.aston.service.UserService;

/**
 * Kafka consumer component for receiving user id from Kafka topic.
 * This component listens to the "payment-user-get-request" Kafka topic and consumes messages containing user id.
 */
@Component
@AllArgsConstructor
public class PaymentUserGetConsumer {
    /**
     * Logger for the {@link PaymentUserGetConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentUserGetConsumer.class);
    private UserService userService;
    private PaymentUserGetProducer paymentUserGetProducer;

    /**
     * Kafka listener method to consume messages from the "payment-user-get-request" topic.
     * @param message The message containing user id.
     */
    @KafkaListener(topics = "payment-user-get-request", groupId = "users-group")
    public void consume(String message) {
        Long userId = Long.valueOf(message);
        log.debug("Received user ID: {} from Payments service", userId);
        UserDto userDto = userService.getUserById(userId);
        paymentUserGetProducer.sendMessage(userDto);
    }
}
