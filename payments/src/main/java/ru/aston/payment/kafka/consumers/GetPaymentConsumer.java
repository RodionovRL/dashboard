package ru.aston.payment.kafka.consumers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.payment.dto.PaymentDto;
import ru.aston.payment.kafka.produsers.GetPaymentProducer;
import ru.aston.payment.service.PaymentService;

/**
 * Kafka consumer component for receiving user id from Kafka topic.
 * This component listens to the "user-get-request" Kafka topic and consumes messages containing user id.
 */
@Component
@RequiredArgsConstructor
public class GetPaymentConsumer {

    /**
     * Logger for the {@link GetPaymentConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(GetPaymentConsumer.class);
    private final PaymentService paymentService;
    private final GetPaymentProducer getPaymentProducer;

    /**
     * Kafka listener method to consume messages from the "payment-get-request" topic.
     * @param message The message containing paymentId.
     */
    @KafkaListener(topics = "payment-get-request", groupId = "payments-group")
    public void consume(String message) {
        Long paymentId = Long.valueOf(message);
        log.debug("Consume get-payments-request: {}", message);
        PaymentDto paymentDto = paymentService.getPaymentById(paymentId);
        getPaymentProducer.sendMessage(paymentDto);
    }
}