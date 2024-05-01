package ru.aston.payment.dto;

import ru.aston.payment.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long executorId;
    private BigDecimal sum;
    private PaymentStatus paymentStatus;
    private LocalDateTime dateTime;
}
