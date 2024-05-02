package ru.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Data transfer object (DTO) representing a payment.
 * This DTO contains information about a payment, including its ID, sum, and date.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PaymentDto {

    /**
     * The ID of the payment.
     */
    private Long id;

    /**
     * The sum of the payment.
     */
    private Long sum;

    /**
     * The orderId of the payment.
     */
    private Long orderId;

    /**
     * The customerId of the payment.
     */
    private Long customerId;

    /**
     * The executorId of the payment.
     */
    private Long executorId;

    private String paymentStatus;

    /**
     * The date and time when the payment was made.
     */
    private LocalDateTime dateTime;
}