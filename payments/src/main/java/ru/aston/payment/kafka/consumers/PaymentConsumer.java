package ru.aston.payment.kafka.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.payment.dto.NewPaymentDto;
import ru.aston.payment.dto.PaymentDto;
import ru.aston.payment.kafka.produsers.PaymentProducer;
import ru.aston.payment.service.PaymentService;

/**
 * Kafka consumer component for receiving user id from Kafka topic.
 * This component listens to the "user-get-request" Kafka topic and consumes messages containing user id.
 */
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    /**
     * Logger for the {@link PaymentConsumer} class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
    private final ObjectMapper mapper;
    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;

    /**
     * Kafka listener method to consume messages from the "payment-request" topic.
     * @param message The message containing paymentDto.
     */
    @KafkaListener(topics = "payment-request", groupId = "payments-group")
    public void consume(String message) throws JsonProcessingException {
        NewPaymentDto newPaymentDto = mapper.readValue(message, NewPaymentDto.class);
        log.debug("Received new inputPaymentDto: {} from Orders service", newPaymentDto);
        PaymentDto paymentDto = paymentService.executePayment(newPaymentDto);
        paymentProducer.sendMessage(paymentDto);
    }
}