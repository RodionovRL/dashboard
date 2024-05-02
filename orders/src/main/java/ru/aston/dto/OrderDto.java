package ru.aston.dto;

import ru.aston.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data transfer object (DTO) representing an order.
 * This DTO contains information about an order, including its ID, name, description,
 * customer details, executor details, payment details, status and date.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    /**
     * The ID of the order.
     */
    private Long id;

    /**
     * The name of the order.
     */
    private String name;

    /**
     * The description of the order.
     */
    private String description;

    /**
     * The details of the customer who placed the order.
     */
    private UserDto customer;

    /**
     * The details of the executor assigned to the order.
     */
    private UserDto executor;

    /**
     * The details of the payment associated with the order.
     */
    private PaymentDto payment;

    /**
     * The status of the order.
     */
    private OrderStatus status;

    /**
     * The date and time when the order was created.
     */
    private LocalDateTime date;

    private BigDecimal sum;
}