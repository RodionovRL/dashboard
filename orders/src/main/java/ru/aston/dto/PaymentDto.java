package ru.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data transfer object (DTO) representing a payment.
 * This DTO contains information about a payment, including its ID, sum, and date.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
     * The date and time when the payment was made.
     */
    private LocalDateTime date;
}